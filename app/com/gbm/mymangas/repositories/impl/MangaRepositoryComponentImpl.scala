package com.gbm.mymangas.repositories.impl

import java.util.UUID

import com.gbm.mymangas.models.filters.Predicate
import com.gbm.mymangas.models.{ Manga, Page }
import com.gbm.mymangas.repositories.MangaRepositoryComponent
import com.gbm.mymangas.utils.json.MangaParser.mangaFormatterRepo
import play.api.libs.json.Json
import play.modules.reactivemongo.json._
import reactivemongo.api.commands.WriteResult

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
 * Created by gbmetzner on 12/8/15.
 */
trait MangaRepositoryComponentImpl extends MangaRepositoryComponent {

  override def mangaRepository: MangaRepository = new MangaRepositoryImpl

  class MangaRepositoryImpl extends MangaRepository {

    override protected[repositories] val collectionName: String = "mangas"

    def insert(manga: Manga): Future[WriteResult] = {
      collection insert manga
    }

    def findBy(predicate: Predicate): Future[List[Manga]] = {
      collection.find(predicate.filter).
        options(predicate.queryOpts).
        sort(predicate.sort).
        cursor[Manga]().collect[List]()
    }

    def findOneBy(predicate: Predicate): Future[Option[Manga]] = {
      collection.find(predicate.filter).sort(predicate.sort).one[Manga]
    }

    def search(predicate: Predicate): Future[Option[Page[Manga]]] = {
      val totalRecordsFuture = collection.find(predicate.filter).cursor[Manga]().collect[List]().map {
        records => records.size
      }

      val itemsFuture = collection.find(predicate.filter)
        .options(predicate.queryOpts)
        .sort(predicate.sort).cursor[Manga]().collect[List](predicate.queryOpts.batchSizeN)

      for {
        totalRecords <- totalRecordsFuture
        items <- itemsFuture
      } yield Option(Page(totalRecords = totalRecords, items = items))
    }

    def update(id: UUID, manga: Manga): Future[WriteResult] = {
      collection.update(Json.obj("id" -> id), manga)
    }

    def remove(id: UUID): Future[WriteResult] = {
      collection.remove(Json.obj("id" -> id))
    }
  }

}
