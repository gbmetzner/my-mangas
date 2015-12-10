package com.gbm.mymangas.actors.mangas

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import com.gbm.mymangas.models.Manga
import com.gbm.mymangas.registries.MangaComponentRegistry
import com.gbm.mymangas.repositories.MangaRepositoryComponent
import com.gbm.mymangas.services.MangaServiceComponent

import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by gbmetzner on 12/9/15.
  */
object PersistMangaActor {

  case class Persist(manga: Manga, id: String)

  case class Persisted(id: String)

  def props(creator: ActorRef): Props = Props(new PersistMangaActor(creator) with MangaComponentRegistry)

}

class PersistMangaActor(creator: ActorRef) extends Actor with ActorLogging {
  requires: MangaServiceComponent with MangaRepositoryComponent =>

  override def receive: Receive = {
    case PersistMangaActor.Persist(manga, id) =>
      mangaService.insert(manga)(mangaRepository.insert)(mangaRepository.findBy).foreach(r => creator ! PersistMangaActor.Persisted(id))
  }

}
