package com.gbm.mymangas.services

import javax.inject.{Inject, Named, Singleton}

import akka.actor.{ActorRef, ActorSystem}
import com.gbm.mymangas.actors.collections.CollectionManager
import com.typesafe.scalalogging.LazyLogging
import play.api.Application

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._

/**
 * @author Gustavo Metzner on 10/19/15.
 */
@Singleton
class SchedulerMangaScraping @Inject()(val app: Application,
                                       val system: ActorSystem,
                                       @Named("collection-manager") val collectionManager: ActorRef)(implicit ec: ExecutionContext)
  extends LazyLogging {

  implicit val application = app

  logger info "Manga Scraping scheduling..."

  system.scheduler.schedule(5.seconds, 24.hours, collectionManager, CollectionManager.Start)
}
