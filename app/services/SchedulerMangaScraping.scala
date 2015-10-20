package services

import javax.inject.{Inject, Named, Singleton}

import actors.collections.CollectionManager
import akka.actor.{ActorRef, ActorSystem}
import play.api.Application

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._

/**
 * @author Gustavo Metzner on 10/19/15.
 */
@Singleton
class SchedulerMangaScraping @Inject()(val app: Application,
                                       val system: ActorSystem,
                                       @Named("collection-manager") val collectionManager: ActorRef)(implicit ec: ExecutionContext) {

  implicit val application = app
  system.scheduler.schedule(0.microseconds, 5.minutes, collectionManager, CollectionManager.Start)
}
