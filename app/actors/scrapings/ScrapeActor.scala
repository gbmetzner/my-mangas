package actors.scrapings

import actors.mangas.MangaManager.ScrapeDone
import actors.scrapings.ScrapeActor.{Scrape, ScraperFactory}
import akka.actor.{Actor, ActorLogging, ActorRef, Props}

/**
 * @author Gustavo Metzner on 10/19/15.
 */
object ScrapeActor {
  def props(creator: ActorRef): Props = Props(new ScrapeActor(creator))

  case class Scrape(publisher: String, collection: String, number: Int, baseURL: String)

  object ScraperFactory {
    def build(publisher: String, collection: String, number: Int, baseURL: String): Scrapable = {
      if ("JBC" == publisher) {
        if ("Fairy Tail" == collection) new FairyTailScraping(collection, number, baseURL)
        else new JBCScrapable(collection, number, baseURL)
      }
      else throw new IllegalArgumentException("")
    }
  }

}

class ScrapeActor(creator: ActorRef) extends Actor with ActorLogging {
  override def receive: Receive = {
    case Scrape(publisher, collection, number, baseURL) =>
      val scraped = ScraperFactory.build(publisher, collection, number, baseURL).scrape
      creator ! ScrapeDone(scraped)
  }
}