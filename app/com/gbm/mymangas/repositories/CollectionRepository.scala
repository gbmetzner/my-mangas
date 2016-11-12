package com.gbm.mymangas.repositories

import java.util.UUID

import com.gbm.mymangas.models.filters.Predicate
import com.gbm.mymangas.models.{Collection, Page}
import com.gbm.mymangas.utils.json.CollectionParser.collectionFormatterRepo
import play.api.libs.json.Json
import play.modules.reactivemongo.json._
import reactivemongo.api.commands.WriteResult

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * Defines a Collection Repository.
  *
  * @author Gustavo Metzner on 12/5/15.
  */
class CollectionRepository extends Repository[Collection] {
  override protected[repositories] val collectionName: String = "collections"

  override def insert(coll: Collection): Future[WriteResult] = collection insert coll

  override def findOneBy(predicate: Predicate): Future[Option[Collection]] = {
    collection.find(predicate.filter).sort(predicate.sort).one[Collection]
  }

  override def findBy(predicate: Predicate): Future[List[Collection]] = {
    collection.find(predicate.filter).sort(predicate.sort).
      cursor[Collection]().collect[List]()
  }

  override def update(id: UUID, coll: Collection): Future[WriteResult] = {
    collection.update(Json.obj("id" -> id), coll)
  }

  override def remove(id: UUID): Future[WriteResult] = {
    collection.remove(Json.obj("id" -> id))
  }

  override def search(predicate: Predicate): Future[Option[Page[Collection]]] = {
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
