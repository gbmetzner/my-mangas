package utils

import java.io.{File, InputStream}

import com.smartfile.api.BasicClient
import org.apache.commons.io.IOUtils
import play.api.libs.json.Json
import utils.Config._

/**
 * Created by gbmetzner on 10/14/15.
 */
object FileUpload {


  private val client = new BasicClient(smartFileKey, smartFileSecret)

  client.setApiUrl(smartFileApiUrl)

  def upload(externalPath: String, file: File): String = {
    makeDirectory(externalPath)
    uploadFile(externalPath, file)
    parsePublicLink(makePublicLink(externalPath, file.getName)) concat file.getName
  }

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
