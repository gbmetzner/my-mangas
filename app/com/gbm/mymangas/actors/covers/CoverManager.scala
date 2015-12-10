package com.gbm.mymangas.actors.covers

import akka.actor._
import com.gbm.mymangas.models.Manga

/**
  * @author Gustavo Metzner on 10/19/15.
  */
object CoverManager {

  case class Start(manga: (Manga, String), id: String)

  case class CoverWorkDone(manga: Manga, id: String)

  def props(creator: ActorRef): Props = Props(new CoverManager(creator))

}

class CoverManager(creator: ActorRef) extends Actor with ActorLogging {

  var coverDownloaders = Map.empty[String, ActorRef]
  var coverUploaders = Map.empty[String, ActorRef]

  override def receive: Receive = {
    case CoverManager.Start((manga, url), id) =>
      createCoverDownloadActor(id) ! CoverDownloader.Download(manga, url, id)

    case CoverDownloader.DownloadDone(manga, filePath, id) =>
      log debug s"Download done. Filepath = $filePath"
      killCoverDownloadActor(id)
      createCoverUploadActor(id) ! CoverUploader.Upload(manga, filePath, id)

    case CoverDownloader.CoverNotAvailable(manga, id) =>
      log debug s"Cover not available for ${manga.fullName}"
      killCoverUploadActor(id)
      creator ! CoverManager.CoverWorkDone(manga, id)

    case CoverUploader.UploadDone(manga, id) =>
      log debug s"Upload done of ${manga.fullName}"
      killCoverUploadActor(id)
      creator ! CoverManager.CoverWorkDone(manga, id)
  }

  def createCoverDownloadActor(id: String): ActorRef = {
    log debug s"Creating CoverDownloadActor with id = $id"
    val actorRef = context.actorOf(CoverDownloader.props(self), s"download-cover-$id")
    coverDownloaders += (s"download-cover-$id" -> actorRef)
    actorRef
  }

  def killCoverDownloadActor(id: String): Unit = {
    log debug s"Killing CoverDownloadActor with id = $id"
    coverDownloaders.get(s"download-cover-$id") match {
      case Some(actorRef) => actorRef ! PoisonPill
      case None => log warning s"Trying to kill CoverDownloadActor id = $id"
    }
  }

  def createCoverUploadActor(id: String): ActorRef = {
    log debug s"Creating CoverUploadActor with id = $id"
    val actorRef = context.actorOf(CoverUploader.props(self), s"upload-cover-$id")
    coverUploaders += (s"upload-cover-$id" -> actorRef)
    actorRef
  }

  def killCoverUploadActor(id: String): Unit = {
    log debug s"Killing CoverUploadActor with id = $id"
    coverUploaders.get(s"upload-cover-$id") match {
      case Some(actorRef) => actorRef ! PoisonPill
      case None => log warning s"Trying to kill CoverUploadActor key = $id"
    }
  }
}
