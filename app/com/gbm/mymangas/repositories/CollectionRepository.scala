package com.gbm.mymangas.repositories

import java.util.UUID
import javax.inject.Inject

import com.gbm.mymangas.models.filters.Predicate
import com.gbm.mymangas.models.{Collection, Page}
import com.gbm.mymangas.utils.json.CollectionParser.collectionFormatterRepo
import play.api.libs.json.Json
import play.modules.reactivemongo.ReactiveMongoApi
import play.modules.reactivemongo.json._
import reactivemongo.api.commands.WriteResult

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * Created by gbmetzner on 12/5/15.
  */
class CollectionRepository @Inject()(val reactiveMongoApi: ReactiveMongoApi) extends Repository {

  override protected[repositories] val collectionName: String = "collections"

  def insert(coll: Collection): Future[WriteResult] = collection insert coll

  def findOneBy(predicate: Predicate): Future[Option[Collection]] = {
    collection.find(predicate.filter).sort(predicate.sort).one[Collection]
  }

  def findBy(predicate: Predicate): Future[List[Collection]] = {
    collection.find(predicate.filter).sort(predicate.sort).
      cursor[Collection]().collect[List]()
  }

  def update(id: UUID, coll: Collection): Future[WriteResult] = {
    collection.update(Json.obj("id" -> id), coll)
  }

  def remove(id: UUID): Future[WriteResult] = {
    collection.remove(Json.obj("id" -> id))
  }

  def search(predicate: Predicate): Future[Option[Page[Collection]]] = {
    val totalRecordsFuture = collection.find(predicate.filter).cursor[Collection]().collect[List]().map {
      records => records.size
    }

    val itemsFuture = collection.find(predicate.filter)
      .options(predicate.queryOpts)
      .sort(predicate.sort).cursor[Collection]().collect[List](predicate.queryOpts.batchSizeN)

    for {
      totalRecords <- totalRecordsFuture
      items <- itemsFuture
    } yield Option(Page(totalRecords = totalRecords, items = items))
  }

}