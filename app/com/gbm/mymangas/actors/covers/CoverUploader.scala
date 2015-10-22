package com.gbm.mymangas.actors.covers

import java.io.File

import com.gbm.mymangas.actors.covers.CoverUploader.Upload
import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import com.gbm.mymangas.models.Manga
import com.gbm.mymangas.utils.FileUpload
import com.gbm.mymangas.utils.StandardizeNames.StandardizeName
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
      log debug s"Uploading cover for ${manga.fullName} of ${manga.collection}"

      val file = new File(filePath)
      val publicLink = FileUpload.upload(s"/my-mangas/mangas/${manga.collection.standardize}", file)
      file.delete()

      creator ! CoverManager.UploadDone(manga.copy(publicLink = publicLink))
  }
}
