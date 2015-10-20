package com.gbm.mymangas.actors.covers

import java.io.File
import java.net.URL

import akka.actor.{ActorLogging, Actor, ActorRef, Props}
import com.gbm.mymangas.models.Manga

import scala.sys.process._

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
      val extension = extractExtension(url)
      val filePath = s"/tmp/${manga.collection}_${manga.number}$extension".replaceAll(" ", "_")
      downloadImage(url, filePath)
      creator ! CoverManager.DownloadDone(manga, filePath)
  }

  def extractExtension(url: String): String = {
    val lastIndexOfDot = url.lastIndexOf(".")
    url.substring(lastIndexOfDot)
  }

  def downloadImage(url: String, file: String) = new URL(url) #> new File(file) !!
}