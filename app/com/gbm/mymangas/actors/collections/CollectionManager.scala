package com.gbm.mymangas.actors.collections

import javax.inject.{Inject, Named, Singleton}

import com.gbm.mymangas.actors.mangas.MangaManager
import akka.actor.{Actor, ActorLogging, ActorRef}
import com.gbm.mymangas.models.filters.CollectionFilter
import com.gbm.mymangas.services.CollectionService

import scala.concurrent.ExecutionContext.Implicits.global

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
    case CollectionManager.Start => collectionService.findBy(CollectionFilter(isComplete = Some(false))).foreach {
      collections => collections.foreach {
        collection => mangaManager ! MangaManager.StartProcess(collection.publisher, collection.name, collection.baseURL)
      }
    }
  }
}

