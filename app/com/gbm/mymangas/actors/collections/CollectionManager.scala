package com.gbm.mymangas.actors.collections

import javax.inject.{Inject, Named, Singleton}

import akka.actor.{Actor, ActorLogging, ActorRef}
import com.gbm.mymangas.actors.mangas.MangaManager
import com.gbm.mymangas.models.filters.CollectionFilter
import com.gbm.mymangas.services.CollectionService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

/**
  * @author Gustavo Metzner on 10/17/15.
  */
object CollectionManager {

  case object Start

}

@Singleton
class CollectionManager @Inject()(collectionService: CollectionService,
                                  @Named("manga-manager") mangaManager: ActorRef) extends Actor with ActorLogging {

  override def receive: Receive = {
    case CollectionManager.Start =>

      log info "Retrieving incomplete collections..."

      startProcess(attempt = 1)
  }

  private def startProcess(attempt: Int): Unit = {
    if (attempt < 4) {
      collectionService.findBy(CollectionFilter(isComplete = Some(false))).onComplete {
        case Success(collections) =>
          collections.foreach {
            collection =>
              log info s" Collection ${collection.name} is being started."
              mangaManager ! MangaManager.StartProcess(collection)
          }
        case Failure(error) =>
          log error error.getMessage
          startProcess(attempt + 1)
      }
    } else {
      log warning s"Something went wrong. $attempt attempts was made. See logs, please."
    }
  }
}

