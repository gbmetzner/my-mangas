package com.gbm.mymangas.actors.covers

import java.io.File

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import com.gbm.mymangas.actors.covers.CoverUploader.Upload
import com.gbm.mymangas.models.Manga
import com.gbm.mymangas.utils.Config
import com.gbm.mymangas.utils.StandardizeNames.StandardizeName
import com.gbm.mymangas.utils.files.upload.{FileUploaderComponent, FileUploaderComponentImpl}

/**
  * @author Gustavo Metzner on 10/19/15.
  */
object CoverUploader {
  def props(creator: ActorRef): Props = Props(new CoverUploader(creator) with FileUploaderComponentImpl)

  case class Upload(manga: Manga, filePath: String, id: String)

  case class UploadDone(manga: Manga, id: String)

}

class CoverUploader(creator: ActorRef) extends Actor with ActorLogging {
  requires: FileUploaderComponent =>

  override def receive: Receive = {
    case Upload(manga, filePath, id) =>
      log debug s"Uploading cover for ${manga.fullName}"
      log debug s"${Config.smartFilePath}my-mangas/mangas/${manga.collection.standardize}"
      log debug s"$filePath"

      val file = new File(filePath)
      val publicLink = fileUploader.upload(s"${Config.smartFilePath}${manga.collection.standardize}", file)

      creator ! CoverUploader.UploadDone(manga.copy(publicLink = publicLink), id)
  }
}
