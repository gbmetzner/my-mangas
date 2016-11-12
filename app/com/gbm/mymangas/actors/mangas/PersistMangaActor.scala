package com.gbm.mymangas.actors.mangas

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import com.gbm.mymangas.models.Manga
import com.gbm.mymangas.registries.MangaComponent
import com.gbm.mymangas.repositories.MangaRepository
import com.gbm.mymangas.services.impl.MangaService

import scala.concurrent.ExecutionContext.Implicits.global

/**
  * @author Gustavo Metzner on 12/9/15.
  */
object PersistMangaActor {

  case class Persist(manga: Manga, id: String)

  case class Persisted(id: String)

  def props(creator: ActorRef, mangaComponent: MangaComponent): Props = {
    Props(new PersistMangaActor(creator, mangaComponent))
  }
}

class PersistMangaActor(creator: ActorRef, mangaComponent: MangaComponent) extends Actor with ActorLogging {

  private val mangaService: MangaService = mangaComponent.mangaService
  private val mangaRepository: MangaRepository = mangaComponent.mangaRepository

  override def receive: Receive = {
    case PersistMangaActor.Persist(manga, id) =>
      mangaService.insert(manga)(mangaRepository.insert)(mangaRepository.findOneBy).foreach(r => creator ! PersistMangaActor.Persisted(id))
  }

}
