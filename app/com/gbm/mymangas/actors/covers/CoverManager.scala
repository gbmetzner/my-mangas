package com.gbm.mymangas.actors.covers

import akka.actor._
import com.gbm.mymangas.actors.mangas.MangaManager
import com.gbm.mymangas.models.Manga
import com.gbm.mymangas.utils.StandardizeNames.StandardizeName
import com.gbm.mymangas.utils.files.download.DefaultFileDownloader
import com.gbm.mymangas.utils.files.upload.SmartFileUploader

/**
  * @author Gustavo Metzner on 10/19/15.
  */
object CoverManager {

  def props(creator: ActorRef): Props = Props(new CoverManager(creator))

  case class Start(mangas: Seq[(Manga, String)])

  case class StartDownload(manga: Manga, link: String)

}

class CoverManager(creator: ActorRef) extends Actor with ActorLogging {

  var coverDownloaders = Map.empty[String, ActorRef]
  var coverUploaders = Map.empty[String, ActorRef]

  override def receive: Receive = {
    case CoverManager.Start(mangas) => mangas.foreach {
      case (manga, link) =>
        val name = s"${manga.collection}_${manga.number}"
        createCoverDownloadActor(name) ! CoverDownloader.Download(manga, link)
    }
    case CoverDownloader.DownloadDone(manga, filePath) =>
      log debug s"Download done. Filepath = $filePath"

      val name = s"${manga.collection}_${manga.number}"
      killCoverDownloadActor(name)
      createCoverUploadActor(name) ! CoverUploader.Upload(manga, filePath)

    case CoverDownloader.CoverNotAvailable(manga) =>
      log debug s"Cover not available for ${manga.fullName}"

      val name = s"${manga.collection}_${manga.number}"
      killCoverUploadActor(name)
      creator ! MangaManager.Persist(manga)

    case CoverUploader.UploadDone(manga) =>
      log debug s"Upload done of ${manga.fullName}"

      val name = s"${manga.collection}_${manga.number}"
      killCoverUploadActor(name)
      creator ! MangaManager.Persist(manga)
  }

  def createCoverDownloadActor(name: String): ActorRef = {
    log debug s"Creating CoverDownloadActor = ${name.standardize}"

    val actorRef = context.actorOf(CoverDownloader.props(self, DefaultFileDownloader), s"download-${name.standardize}")
    coverDownloaders += (s"download-$name" -> actorRef)
    actorRef
  }

  def killCoverDownloadActor(key: String): Unit = {
    log debug s"Killing CoverDownloadActor = ${key.standardize}"

    coverDownloaders.get(s"download-$key") match {
      case Some(actorRef) => actorRef ! PoisonPill
      case None => log warning s"Trying to kill CoverDownloadActor key = ${key.standardize}"
    }
  }

  def createCoverUploadActor(name: String): ActorRef = {
    log debug s"Creating CoverUploadActor = ${name.standardize}"

    val actorRef = context.actorOf(CoverUploader.props(self, SmartFileUploader), s"upload-${name.standardize}")
    coverUploaders += (s"upload-$name" -> actorRef)
    actorRef
  }

  def killCoverUploadActor(key: String): Unit = {
    log debug s"Killing CoverUploadActor = ${key.standardize}"

    coverUploaders.get(s"upload-$key") match {
      case Some(actorRef) => actorRef ! PoisonPill
      case None => log warning s"Trying to kill CoverUploadActor key = ${key.standardize}"
    }
  }

}
