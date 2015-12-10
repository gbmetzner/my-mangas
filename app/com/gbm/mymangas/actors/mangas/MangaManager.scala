package com.gbm.mymangas.actors.mangas

import akka.actor._
import com.gbm.mymangas.actors.covers.CoverManager
import com.gbm.mymangas.actors.scrapings.ScrapeMangaActor
import com.gbm.mymangas.actors.{LinksActor, PagesActor}
import com.gbm.mymangas.models.Collection
import com.gbm.mymangas.registries.MangaComponentRegistry
import com.gbm.mymangas.repositories.MangaRepositoryComponent
import com.gbm.mymangas.services.MangaServiceComponent
import com.gbm.mymangas.utils.StandardizeNames._
import com.gbm.mymangas.utils.UUID._
import com.gbm.mymangas.utils.browser.DefaultBrowser

import scala.concurrent.ExecutionContext.Implicits.global

/**
  * @author Gustavo Metzner on 10/17/15.
  */
object MangaManager {

  case class StartProcess(collection: Collection)

  def props: Props = Props(new MangaManager with MangaComponentRegistry)

}

class MangaManager extends Actor with ActorLogging {
  requires: MangaServiceComponent with MangaRepositoryComponent =>

  val coverManager = context.actorOf(CoverManager.props(self), "cover-manager")

  var pagesActors: Map[String, ActorRef] = Map.empty[String, ActorRef]
  var linksActors: Map[String, ActorRef] = Map.empty[String, ActorRef]
  var scrapeActors: Map[String, ActorRef] = Map.empty[String, ActorRef]
  var persistActors: Map[String, ActorRef] = Map.empty[String, ActorRef]

  override def receive: Receive = {
    case MangaManager.StartProcess(collection) =>
      val latestNumberFuture = mangaService.latestNumber(collection.name)(mangaRepository.findOneBy).map {
        case Some(manga) => manga.number
        case None => 0
      }

      for {
        number <- latestNumberFuture
      } {
        val id = actorId(collection.name).standardize
        createPagesActor(id) ! PagesActor.FindPages(collection, number, id)
      }

    case PagesActor.Pages(collection, pages, id) =>
      killPagesActor(id)
      pages.foreach {
        page =>
          val id = actorId(collection.name).standardize
          createLinksActor(id) ! LinksActor.FindLinks(collection, page, id)
      }

    case LinksActor.Links(collection, links, id) =>
      killLinksActor(id)
      links.foreach {
        doc =>
          val id = actorId(collection.name).standardize
          createScrapeActor(id) ! ScrapeMangaActor.Scrape(collection, doc, id)
      }

    case ScrapeMangaActor.ScrapeDone(manga, url, id) =>
      killScrapeActor(id)
      coverManager ! CoverManager.Start(manga -> url, id)

    case CoverManager.CoverWorkDone(manga, id) => createPersistActor(id) ! PersistMangaActor.Persist(manga, id)

    case PersistMangaActor.Persisted(id) => killPersistActor(id)
  }

  def createPagesActor(id: String): ActorRef = {
    log debug s"Creating page-actor-$id"
    val actorRef = context.actorOf(PagesActor.props(self), s"page-actor-$id")
    pagesActors += (s"page-actor-$id" -> actorRef)
    actorRef
  }

  def killPagesActor(id: String): Unit = {
    log debug s"Killing page-actor-$id"
    pagesActors.get(s"page-actor-$id") match {
      case Some(actorRef) => actorRef ! PoisonPill
      case None => log warning s"There is no page-actor-$id to kill."
    }
  }

  def createLinksActor(id: String): ActorRef = {
    log debug s"Creating link-actor-$id"
    val actorRef = context.actorOf(LinksActor.props(self, DefaultBrowser), s"link-actor-$id")
    linksActors += (s"link-actor-$id" -> actorRef)
    actorRef
  }

  def killLinksActor(id: String): Unit = {
    log debug s"Killing link-actor-$id"
    linksActors.get(s"link-actor-$id") match {
      case Some(actorRef) => actorRef ! PoisonPill
      case None => log warning s"There is no link-actor-$id to kill."
    }
  }

  def createScrapeActor(id: String): ActorRef = {
    log debug s"Creating scrape-actor-$id"
    val actorRef = context.actorOf(ScrapeMangaActor.props(self), s"scrape-actor-$id")
    scrapeActors += (s"scrape-actor-$id" -> actorRef)
    actorRef
  }

  def killScrapeActor(id: String): Unit = {
    log debug s"Killing scrape-actor-$id"
    scrapeActors.get(s"scrape-actor-$id") match {
      case Some(actorRef) => actorRef ! PoisonPill
      case None => log warning s"There is no scrape-actor-$id to kill."
    }
  }

  def createPersistActor(id: String): ActorRef = {
    log debug s"Creating persist-actor-$id"
    val actorRef = context.actorOf(PersistMangaActor.props(self), s"persist-actor-$id")
    persistActors += (s"persist-actor-$id" -> actorRef)
    actorRef
  }

  def killPersistActor(id: String): Unit = {
    log debug s"Killing persist-actor-$id"
    persistActors.get(s"persist-actor-$id") match {
      case Some(actorRef) => actorRef ! PoisonPill
      case None => log warning s"There is no persist-actor-$id to kill."
    }
  }
}