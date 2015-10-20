package actors.mangas

import javax.inject.{Inject, Singleton}

import actors.covers.CoverManager
import actors.scrapings.ScrapeActor
import akka.actor.{Actor, ActorLogging, ActorRef, PoisonPill}
import models.Manga
import services.MangaService

import scala.concurrent.ExecutionContext.Implicits.global

/**
 * @author Gustavo Metzner on 10/17/15.
 */
object MangaManager {

  case class StartProcess(publisher: String, collection: String, baseURL: String)

  case class PersistManga(mangas: List[Manga])

  case class GetCovers(mangas: Seq[(Manga, String)])

  case class ScrapeDone(mangas: Seq[(Manga, String)])

  case class Persist(manga: Manga)

}

@Singleton
class MangaManager @Inject()(mangaService: MangaService) extends Actor with ActorLogging {

  val coverManager = context.actorOf(CoverManager.props(self), "cover-manager")

  var scrapeActors: Map[String, ActorRef] = Map.empty[String, ActorRef]

  override def receive: Receive = {

    case MangaManager.StartProcess(publisher, collection, baseURL) => mangaService.latestNumber(collection).map {
      case Some(manga) =>
        println(manga.number.toInt)
        manga.number.toInt
      case None =>
        println(0)
        0
    }.foreach {
      number => //scrapeActor(collection) ! ScrapeActor.Scrape(publisher, collection, number, baseURL)
    }

    case MangaManager.ScrapeDone(mangas) =>
      mangas.headOption.foreach { case (manga, _) => killScrapeActor(manga.collection) }
      coverManager ! CoverManager.Start(mangas)

    case MangaManager.Persist(manga) => mangaService insert manga

  }

  def scrapeActor(name: String): ActorRef = {
    log debug s"Scrap Actor name = $name"

    val actorRef = context.actorOf(ScrapeActor.props(self), name.toLowerCase.replaceAll(" ", "_"))

    scrapeActors += (name -> actorRef)

    actorRef
  }

  def killScrapeActor(key: String): Unit = scrapeActors.get(key) match {
    case Some(actorRef) => actorRef ! PoisonPill
    case None => log warning s"Trying to kill actor key = $key"
  }

}