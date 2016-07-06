package com.gbm.mymangas.actors.scrapings

import akka.actor.{ Actor, ActorLogging, ActorRef, Props }
import com.gbm.mymangas.actors.scrapings.ScrapeMangaActor.Scrape
import com.gbm.mymangas.models.{ Collection, Manga }
import com.gbm.mymangas.scrapes.mangas.{ JBCMangaScraper, MangaScraper, PaniniMangaScraper }
import net.ruippeixotog.scalascraper.model.Document

/**
 * @author Gustavo Metzner on 10/19/15.
 */
object ScrapeMangaActor {

  def props(creator: ActorRef): Props = Props(new ScrapeMangaActor(creator))

  case class Scrape(collection: Collection, document: Document, id: String)

  case class ScrapeDone(manga: Manga, url: String, id: String)

  def findMangaScraper(publisherName: String): MangaScraper = {
    if ("JBC" == publisherName) JBCMangaScraper
    else PaniniMangaScraper
  }

}

class ScrapeMangaActor(creator: ActorRef) extends Actor with ActorLogging {

  override def receive: Receive = {
    case Scrape(collection, document, id) =>
      val mangaScraper = ScrapeMangaActor.findMangaScraper(collection.publisher)
      val (manga, url) = mangaScraper.scrape(document)
      creator ! ScrapeMangaActor.ScrapeDone(manga.copy(collection = collection.name), url, id)
  }
}
