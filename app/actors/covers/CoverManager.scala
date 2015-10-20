package actors.covers

import actors.mangas.MangaManager
import akka.actor._
import models.Manga

/**
 * @author Gustavo Metzner on 10/19/15.
 */
object CoverManager {

  def props(creator: ActorRef): Props = Props(new CoverManager(creator))

  case class Start(mangas: Seq[(Manga, String)])

  case class DownloadDone(manga: Manga, filePath: String)

  case class StartDownload(manga: Manga, link: String)

  case class UploadDone(manga: Manga)

}

class CoverManager(creator: ActorRef) extends Actor with ActorLogging {

  var coverDownloaders = Map.empty[String, ActorRef]
  var coverUploaders = Map.empty[String, ActorRef]

  override def receive: Receive = {
    case CoverManager.Start(mangas) => mangas.foreach {
      case (manga, link) => coverDownloadActor(s"download_${manga.collection}_${manga.number}".replaceAll(" ", "_")) ! CoverDownloader.Download(manga, link)
    }
    case CoverManager.DownloadDone(manga, filePath) => {
      val name = s"download_${manga.collection}_${manga.number}".replaceAll(" ", "_")
      val name2 = s"upload_${manga.collection}_${manga.number}".replaceAll(" ", "_")
      killCoverDownloadActor(name)
      coverUploadActor(name2) ! CoverUploader.Upload(manga, filePath)
    }
    case CoverManager.UploadDone(manga) => {
      val name = s"upload_${manga.collection}_${manga.number}".replaceAll(" ", "_")
      killCoverUploadActor(name)
      creator ! MangaManager.Persist(manga)
    }
  }

  def coverDownloadActor(name: String): ActorRef = {
    val actorRef = context.actorOf(CoverDownloader.props(self), name)
    coverDownloaders += (name -> actorRef)
    actorRef
  }

  def killCoverDownloadActor(key: String): Unit = coverDownloaders.get(key) match {
    case Some(actorRef) => actorRef ! PoisonPill
    case None => //log warning s"Trying to kill actor key = $key"
  }

  def coverUploadActor(name: String): ActorRef = {
    val actorRef = context.actorOf(CoverUploader.props(self), name)
    coverUploaders += (name -> actorRef)
    actorRef
  }

  def killCoverUploadActor(key: String): Unit = coverUploaders.get(key) match {
    case Some(actorRef) => actorRef ! PoisonPill
    case None => //log warning s"Trying to kill actor key = $key"
  }

}
