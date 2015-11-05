package com.gbm.mymangas.utils

import java.io.{File, InputStream}

import com.gbm.mymangas.utils.Config._
import com.sksamuel.scrimage.Image
import com.smartfile.api.BasicClient
import org.apache.commons.io.IOUtils
import play.api.libs.json.Json

import scala.util.{Failure, Success, Try}

/**
  * @author Gustavo Metzner on 10/14/15.
  */
object FileUpload {


  private val client = new BasicClient(smartFileKey, smartFileSecret)

  client.setApiUrl(smartFileApiUrl)

  def upload(externalPath: String, file: File): String = {
    Try {
      makeDirectory(externalPath)
      resize(file)
      uploadFile(externalPath, file)
      parsePublicLink(makePublicLink(externalPath, file.getName)) concat file.getName
    } match {
      case Success(publicLink) => publicLink
      case Failure(_) => Config.defaultMangaImage
    }
  }

  private def resize(file: File): Unit = Image.fromFile(file).cover(200, 304).output(file)

  private def makeDirectory(directory: String): Unit = {
    responseToString(client.put(s"/path/oper/mkdir/$directory", null, ""))
  }

  private def uploadFile(externalPath: String, file: File): Unit = {
    client.post(s"/path/data$externalPath", null, file)
  }

  private def makePublicLink(externalPath: String, fileName: String): String = {
    responseToString(client.post(s"/link/", null, s"path=$externalPath/$fileName"))
  }

  private def responseToString(inputStream: InputStream): String = {
    if (inputStream == null) " "
    else IOUtils.toString(inputStream)
  }

  private def parsePublicLink(response: String): String = {
    (Json.parse(response) \ "href").as[String]
  }
}
