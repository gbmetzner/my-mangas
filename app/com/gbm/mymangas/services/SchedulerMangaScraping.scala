package com.gbm.mymangas.services

import javax.inject.{Inject, Named, Singleton}

import com.gbm.mymangas.actors.collections.CollectionManager
import akka.actor.{ActorRef, ActorSystem}
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

  logger debug "Manga Scraping scheduling..."

  system.scheduler.schedule(0.microseconds, 5.minutes, collectionManager, CollectionManager.Start)
}
