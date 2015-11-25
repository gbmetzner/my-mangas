package com.gbm.mymangas.actors.mangas

import javax.inject.{Inject, Singleton}

import akka.actor._
import com.gbm.mymangas.actors.PagesActor
import com.gbm.mymangas.actors.covers.CoverManager
import com.gbm.mymangas.actors.scrapings.ScrapingActor
import com.gbm.mymangas.models.{Collection, Manga}
import com.gbm.mymangas.services.MangaService
import com.gbm.mymangas.utils.StandardizeNames.StandardizeName

import scala.concurrent.ExecutionContext.Implicits.global

/**
  * @author Gustavo Metzner on 10/17/15.
  */
object MangaManager {

  case class StartProcess(collection: Collection)

  case class PersistManga(mangas: List[Manga])

  case class GetCovers(mangas: Seq[(Manga, String)])

  case class ScrapingDone(collection: String, mangas: Seq[(Manga, String)])

  case class Persist(manga: Manga)

}

@Singleton
class MangaManager @Inject()(mangaService: MangaService) extends Actor with ActorLogging {

  val coverManager = context.actorOf(CoverManager.props(self), "cover-manager")

  var scrapeActors: Map[String, ActorRef] = Map.empty[String, ActorRef]
  var pagesActors: Map[String, ActorRef] = Map.empty[String, ActorRef]

  override def receive: Receive = {

    case MangaManager.StartProcess(collection) =>
      val latestNumberFuture = mangaService.latestNumber(collection.name).map {
        case Some(manga) => manga.number
        case None => 0
      }

      for {
        number <- latestNumberFuture
      } (createPagesActor(collection.name)
        ! PagesActor.messages.FindPages(collection.publisher, collection.searchParam, number))

    case PagesActor.messages.Pages(searchParam, pages) =>
      killPagesActor(searchParam)
      println(pages.size)
  }

  def createPagesActor(name: String): ActorRef = {
    log debug s"Creating PageActor = ${name.standardize}"

    val actorRef = context.actorOf(PagesActor.props(self), name.standardize)

    pagesActors += (name.standardize -> actorRef)

    actorRef
  }

  def killPagesActor(name: String): Unit = {

    log debug s"Killing PagesActor = ${name.standardize}"

    pagesActors.get(name.standardize) match {
      case Some(actorRef) => actorRef ! PoisonPill
      case None => log warning s"There is no PagesActor ${name.standardize} to kill."
    }
  }
}