package com.gbm.mymangas.actors

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import com.gbm.mymangas.actors.mangas.MangaManager
import com.gbm.mymangas.models.filters.CollectionFilter
import com.gbm.mymangas.registries.CollectionComponent
import com.gbm.mymangas.repositories.CollectionRepository
import com.gbm.mymangas.services.CollectionService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

/**
  * @author Gustavo Metzner on 10/17/15.
  */
object MangasScraper {

  case object Start

  def props: Props = Props(new MangasScraper(new CollectionComponent(new CollectionService, new CollectionRepository)))

}

class MangasScraper(collectionComponent: CollectionComponent) extends Actor with ActorLogging {

  val collectionService: CollectionService = collectionComponent.collectionService
  val collectionRepository: CollectionRepository = collectionComponent.collectionRepository

  val mangaManager: ActorRef = context.actorOf(MangaManager.props, "manga-manager")

  override def receive: Receive = {
    case MangasScraper.Start =>
      log info "Retrieving incomplete collections..."
      startProcess(attempt = 1)

    case message => log warning s"Something went wrong. The $message should have been caught."

  }

  private def startProcess(attempt: Int): Unit = {
    if (attempt < 4) {
      collectionService.findBy(CollectionFilter(isComplete = Some(false)))(collectionRepository.findBy).onComplete {
        case Success(collections) => collections.foreach {
          collection =>
            log info s"Collection ${collection.name} has been started."
            mangaManager ! MangaManager.StartProcess(collection)
        }
        case Failure(error) =>
          log error error.getMessage
          startProcess(attempt + 1)
      }
    } else {
      log warning s"Something went wrong. $attempt attempts were made. See logs, please."
    }
  }
}

