package com.gbm.mymangas.actors.covers

import java.io.File
import java.net.URL

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import com.gbm.mymangas.models.Manga
import com.gbm.mymangas.utils.Config
import com.gbm.mymangas.utils.StandardizeNames.StandardizeName

import scala.sys.process._
import scala.util.{Failure, Success, Try}

/**
 * @author Gustavo Metzner on 10/19/15.
 */
object CoverDownloader {

  def props(creator: ActorRef): Props = Props(new CoverDownloader(creator))

  case class Download(manga: Manga, url: String)

}

class CoverDownloader(creator: ActorRef) extends Actor with ActorLogging {

  override def receive: Receive = {
    case CoverDownloader.Download(manga, url) =>
      log debug s"Downloading ${manga.fullName} from $url"
      Try {
        val extension = extractExtension(url)
        val filePath = s"/tmp/${manga.collection}_${manga.number}$extension".standardize
        downloadImage(url, filePath)
        filePath
      } match {
        case Success(filePath) => creator ! CoverManager.DownloadDone(manga, filePath)
        case Failure(_) =>
          log info s"Cover not found for ${manga.fullName}"
          creator ! CoverManager.CoverNotAvailable(manga.copy(publicLink = Config.defaultMangaImage))
      }
  }

  def extractExtension(url: String): String = {
    val lastIndexOfDot = url.lastIndexOf(".")
    val extension = url.substring(lastIndexOfDot)
    if (extension.length > 4) ".jpg" else extension
  }

  def downloadImage(url: String, file: String) = {
    log debug s"Downloading from $url to $file"
    new URL(url) #> new File(file) !!
  }
}