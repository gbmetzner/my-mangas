package com.gbm.mymangas.repositories.impl

import java.util.UUID

import com.gbm.mymangas.models.filters.Predicate
import com.gbm.mymangas.models.{Page, Publisher}
import com.gbm.mymangas.repositories.PublisherRepository
import com.gbm.mymangas.utils.json.PublisherParser.publisherFormatterRepo
import play.api.libs.json.Json
import play.modules.reactivemongo.json._
import reactivemongo.api.commands.WriteResult

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * Created by gbmetzner on 12/8/15.
  */
class PublisherRepositoryImpl extends PublisherRepository {

  /**
    * The collection's name.
    *
    * @return The name of the collection.
    */
  override protected[repositories] def collectionName: String = "publishers"

  /**
    * Insert a [[Publisher]].
    *
    * @param publisher The [[Publisher]] to be inserted.
    * @return A [[Future]] of [[WriteResult]]
    */
  override def insert(publisher: Publisher): Future[WriteResult] = collection insert publisher

  /**
    * Remove a [[Publisher]].
    *
    * @param id The [[UUID]] of the [[Publisher]].
    * @return A [[Future]] of [[WriteResult]]
    */
  override def remove(id: UUID): Future[WriteResult] = collection.remove(Json.obj("id" -> id))

  /**
    * Search [[Publisher]]'s by a predicate.
    *
    * @param predicate The [[Predicate]] to the filter be based on.
    * @return A [[Future]] of [[Option]] of [[Page]] of a [[Publisher]].
    */
  override def search(predicate: Predicate): Future[Option[Page[Publisher]]] = {
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

  /**
    * Update a [[Publisher]].
    *
    * @param id        The [[UUID]] of the [[Publisher]] to be updated.
    * @param publisher The [[Publisher]] to be updated.
    * @return A [[Future]] of [[WriteResult]]
    */
  override def update(id: UUID, publisher: Publisher): Future[WriteResult] = collection.update(Json.obj("id" -> id), publisher)

  /**
    * Find only one [[Publisher]].
    *
    * @param predicate The [[Predicate]] to the filter be based on.
    * @return A [[Future]] of [[Option]] of a [[Publisher]].
    */
  override def findOneBy(predicate: Predicate): Future[Option[Publisher]] = collection.find(predicate.filter).sort(predicate.sort).one[Publisher]

  /**
    * Find [[Publisher]]'s by a predicate.
    *
    * @param predicate The [[Predicate]] to the filter be based on.
    * @return A [[Future]] of [[List]] of a [[Publisher]].
    */
  override def findBy(predicate: Predicate): Future[List[Publisher]] = collection.find(predicate.filter).
    sort(predicate.sort).
    cursor[Publisher]().collect[List]()
}
