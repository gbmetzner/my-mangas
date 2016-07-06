package com.gbm.mymangas.services

import javax.inject.{ Inject, Named, Singleton }

import akka.actor.{ ActorRef, ActorSystem }
import com.gbm.mymangas.actors.MangasScraper
import com.gbm.mymangas.repositories.MangaRepositoryComponent
import com.typesafe.scalalogging.LazyLogging
import play.api.Application

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._

/**
 * @author Gustavo Metzner on 10/19/15.
 */
@Singleton
class SchedulerMangaScraping @Inject() (
    val app: Application,
    val system: ActorSystem
)(implicit ec: ExecutionContext) extends LazyLogging {

  implicit val application = app

  logger info s"Scheduling Manga Scraping..."

  val mangasScraper = system.actorOf(MangasScraper.props, "manga-scraper")

  system.scheduler.schedule(15.seconds, 24.hours, mangasScraper, MangasScraper.Start)
}
