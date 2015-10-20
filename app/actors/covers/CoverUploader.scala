package actors.covers

import java.io.File

import actors.covers.CoverUploader.Upload
import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import models.Manga
import utils.FileUpload

/**
 * @author Gustavo Metzner on 10/19/15.
 */
object CoverUploader {
  def props(creator: ActorRef): Props = Props(new CoverUploader(creator))

  case class Upload(manga: Manga, filePath: String)

}

class CoverUploader(creator: ActorRef) extends Actor with ActorLogging {

  override def receive: Receive = {
    case Upload(manga, filePath) =>
      val file = new File(filePath)
      val publicLink = FileUpload.upload(s"/my-mangas/mangas/${manga.collection.toLowerCase.replaceAll(" ", "_")}", file)
      file.delete()
      creator ! CoverManager.UploadDone(manga.copy(publicLink = publicLink))
  }
}
