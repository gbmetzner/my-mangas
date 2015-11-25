package com.gbm.mymangas.actors.scrapings

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import com.gbm.mymangas.actors.mangas.MangaManager.ScrapingDone
import com.gbm.mymangas.actors.scrapings.ScrapingActor.{Scrape, ScraperFactory}

/**
  * @author Gustavo Metzner on 10/19/15.
  */
object ScrapingActor {
  def props(creator: ActorRef): Props = Props(new ScrapingActor(creator))

  case class Scrape(publisher: String, collection: String, number: Int, searchParam: String)

  object ScraperFactory {
    def build(publisher: String, collection: String, number: Int, searchParam: String): Scrapable = {
      if ("JBC" == publisher) new JBCScraping(collection, number, searchParam)
      else if ("Panini" == publisher) new PaniniScraping(collection, number, searchParam)
      else throw new IllegalArgumentException()
    }
  }

}

class ScrapingActor(creator: ActorRef) extends Actor with ActorLogging {

  override def receive: Receive = {
    case Scrape(publisher, collection, number, searchParam) =>

      log debug s"Scraping $publisher, $collection, $number and $searchParam"

      val scraped = ScraperFactory.build(publisher, collection, number, searchParam).scrape
      creator ! ScrapingDone(collection, scraped)
  }
}