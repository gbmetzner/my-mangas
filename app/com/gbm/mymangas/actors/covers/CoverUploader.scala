package com.gbm.mymangas.actors.covers

import java.io.File

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import com.gbm.mymangas.actors.covers.CoverUploader.Upload
import com.gbm.mymangas.models.Manga
import com.gbm.mymangas.utils.StandardizeNames.StandardizeName
import com.gbm.mymangas.utils.files.upload.FileUploader

/**
  * @author Gustavo Metzner on 10/19/15.
  */
object CoverUploader {
  def props(creator: ActorRef, fileUpload: FileUploader): Props = Props(new CoverUploader(creator, fileUpload))

  case class Upload(manga: Manga, filePath: String)

  case class UploadDone(manga: Manga)

}

class CoverUploader(creator: ActorRef, fileUpload: FileUploader) extends Actor with ActorLogging {

  override def receive: Receive = {
    case Upload(manga, filePath) =>
      log debug s"Uploading cover for ${manga.fullName}"
      log debug s"/my-mangas/mangas/${manga.collection.standardize}"
      log debug s"$filePath"

      val file = new File(filePath)
      val publicLink = fileUpload.upload(s"/my-mangas/mangas/${manga.collection.standardize}", file)

      creator ! CoverUploader.UploadDone(manga.copy(publicLink = publicLink))
  }
}
