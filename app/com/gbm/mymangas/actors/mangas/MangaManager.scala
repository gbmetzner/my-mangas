package com.gbm.mymangas.actors.mangas

import javax.inject.{Inject, Singleton}

import akka.actor.SupervisorStrategy.{Decider, Restart, Stop}
import akka.actor._
import com.gbm.mymangas.actors.covers.CoverManager
import com.gbm.mymangas.actors.scrapings.ScrapingActor
import com.gbm.mymangas.models.Manga
import com.gbm.mymangas.services.MangaService
import com.gbm.mymangas.utils.StandardizeNames.StandardizeName

import scala.concurrent.ExecutionContext.Implicits.global

/**
 * @author Gustavo Metzner on 10/17/15.
 */
object MangaManager {

  case class StartProcess(publisher: String, collection: String, searchParam: String)

  case class PersistManga(mangas: List[Manga])

  case class GetCovers(mangas: Seq[(Manga, String)])

  case class ScrapingDone(collection: String, mangas: Seq[(Manga, String)])

  case class Persist(manga: Manga)

}

@Singleton
class MangaManager @Inject()(mangaService: MangaService) extends Actor with ActorLogging {

  val coverManager = context.actorOf(CoverManager.props(self), "cover-manager")

  var scrapeActors: Map[String, ActorRef] = Map.empty[String, ActorRef]

  override def receive: Receive = {

    case MangaManager.StartProcess(publisher, collection, searchParam) => mangaService.latestNumber(collection).map {
      case Some(manga) => manga.number
      case None => 0
    }.foreach {
      number => createScrapingActor(collection) ! ScrapingActor.Scrape(publisher, collection, number, searchParam)
    }

    case MangaManager.ScrapingDone(collection, mangas) =>
      val mangasSize = mangas.size

      log debug s"Scraping done. $mangasSize mangas has been found"

      killScrapingActor(collection)

      coverManager ! CoverManager.Start(mangas)

    case MangaManager.Persist(manga) =>
      log debug s"Persisting ${manga.fullName}..."

      mangaService insert manga
  }

  def createScrapingActor(name: String): ActorRef = {
    log debug s"Creating ScrapingActor = $name"

    val actorRef = context.actorOf(ScrapingActor.props(self), name.standardize)

    scrapeActors += (name.standardize -> actorRef)

    actorRef
  }

  def killScrapingActor(key: String): Unit = {

    log debug s"Killing ScrapingActor = $key"

    scrapeActors.get(key.standardize) match {
      case Some(actorRef) => actorRef ! PoisonPill
      case None => log warning s"Trying to kill actor key = $key"
    }
  }

}