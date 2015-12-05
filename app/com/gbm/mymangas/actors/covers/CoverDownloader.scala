package com.gbm.mymangas.actors.covers

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import com.gbm.mymangas.models.Manga
import com.gbm.mymangas.utils.Config
import com.gbm.mymangas.utils.StandardizeNames.StandardizeName
import com.gbm.mymangas.utils.files.download.FileDownloader

import scala.util.{Failure, Success, Try}

/**
  * @author Gustavo Metzner on 10/19/15.
  */
object CoverDownloader {

  def props(creator: ActorRef, fileDownloader: FileDownloader): Props = Props(new CoverDownloader(creator, fileDownloader))

  case class Download(manga: Manga, url: String)

  case class DownloadDone(manga: Manga, filePath: String)

  case class CoverNotAvailable(manga: Manga)

}

class CoverDownloader(creator: ActorRef, fileDownloader: FileDownloader) extends Actor with ActorLogging {

  override def receive: Receive = {
    case CoverDownloader.Download(manga, url) =>
      log debug s"Downloading ${manga.fullName} from $url"
      Try {
        val extension = fileDownloader.extractExtension(url)
        val filePath = s"/tmp/${manga.collection}_${manga.number}$extension".standardize
        fileDownloader.downloadImage(url, filePath)
        filePath
      } match {
        case Success(filePath) => creator ! CoverDownloader.DownloadDone(manga, filePath)
        case Failure(_) =>
          log info s"Cover not found for ${manga.fullName}"
          creator ! CoverDownloader.CoverNotAvailable(manga.copy(publicLink = "manga.no.cover"))
      }
  }
}