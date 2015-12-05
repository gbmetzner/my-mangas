package com.gbm.mymangas.utils.files.upload

import java.io.{File, InputStream}

import com.gbm.mymangas.utils.{Image, Config}
import com.gbm.mymangas.utils.Config._
import com.smartfile.api.BasicClient
import org.apache.commons.io.IOUtils
import play.api.libs.json.Json

import scala.util.{Failure, Success, Try}

/**
  * Created by gbmetzner on 12/4/15.
  */
trait FileUploader {
  def upload(externalPath: String, file: File): String

  object smartfile {

    private val client = new BasicClient(smartFileKey, smartFileSecret)

    client.setApiUrl(smartFileApiUrl)

    def upload(externalPath: String, file: File)
              (f: String => Unit)(g: File => Unit)
              (h: (String, File) => Unit)
              (i: (String, String) => String)
              (j: String => String): String = {
      Try {
        f(externalPath)
        g(file)
        h(externalPath, file)
        val result = j(i(externalPath, file.getName)) concat file.getName
        file.delete()
        result
      } match {
        case Success(publicLink) => publicLink
        case Failure(_) => Config.defaultCover
      }
    }

    def resize(file: File): Unit = Image.resize(file)(204, 300)

    def makeDirectory(directory: String): Unit = {
      client.put(s"/path/oper/mkdir/$directory", null, "")
    }

    def uploadFile(externalPath: String, file: File): Unit = {
      client.post(s"/path/data$externalPath", null, file)
    }

    def makePublicLink(externalPath: String, fileName: String): String = {
      responseToString(client.post(s"/link/", null, s"path=$externalPath/$fileName"))
    }

    protected def responseToString(inputStream: InputStream): String = {
      if (inputStream == null) " "
      else IOUtils.toString(inputStream)
    }

    def parsePublicLink(response: String): String = {
      (Json.parse(response) \ "href").as[String]
    }

  }

}
