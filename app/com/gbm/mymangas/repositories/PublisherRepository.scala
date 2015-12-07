package com.gbm.mymangas.repositories

import java.util.UUID
import javax.inject.Inject

import com.gbm.mymangas.models.filters.Predicate
import com.gbm.mymangas.models.{Page, Publisher}
import com.gbm.mymangas.utils.json.PublisherParser.publisherFormatterRepo
import play.api.libs.json.Json
import play.modules.reactivemongo.ReactiveMongoApi
import play.modules.reactivemongo.json._
import reactivemongo.api.commands.WriteResult

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * Created by gbmetzner on 12/6/15.
  */
class PublisherRepository @Inject()(val reactiveMongoApi: ReactiveMongoApi) extends Repository {

  override protected[repositories] val collectionName: String = "publishers"

  def insert(publisher: Publisher): Future[WriteResult] = {
    collection insert publisher
  }

  def update(id: UUID, publisher: Publisher): Future[WriteResult] = {
    collection.update(Json.obj("id" -> id), publisher)
  }

  def findBy(predicate: Predicate): Future[List[Publisher]] = {
    collection.find(predicate.filter).
      sort(predicate.sort).
      cursor[Publisher]().collect[List]()
  }

  def findOneBy(predicate: Predicate): Future[Option[Publisher]] = {
    collection.find(predicate.filter).sort(predicate.sort).one[Publisher]
  }

  def remove(id: UUID): Future[WriteResult] = {
    collection.remove(Json.obj("id" -> id))
  }

  def search(predicate: Predicate): Future[Option[Page[Publisher]]] = {
    val totalRecordsFuture = collection.find(predicate.filter).cursor[Publisher]().collect[List]().map {
      records => records.size
    }
    val itemsFuture = collection.find(predicate.filter)
      .options(predicate.queryOpts)
      .sort(predicate.sort).cursor[Publisher]().collect[List](predicate.queryOpts.batchSizeN)

    for {
      totalRecords <- totalRecordsFuture
      items <- itemsFuture
    } yield Option(Page(totalRecords = totalRecords, items = items))
  }

}
