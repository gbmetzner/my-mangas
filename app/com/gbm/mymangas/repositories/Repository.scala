package com.gbm.mymangas.repositories

import java.util.UUID

import com.gbm.mymangas.models.Page
import com.gbm.mymangas.models.filters.Predicate
import com.gbm.mymangas.utils.Config
import com.typesafe.scalalogging.LazyLogging
import reactivemongo.api._
import reactivemongo.api.commands.WriteResult
import reactivemongo.play.json.collection.JSONCollection

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{ Await, Future }

/**
 * Created by gbmetzner on 12/5/15.
 */
trait Repository[T] extends LazyLogging {

  /**
   * The collection's name.
   *
   * @return The name of the collection.
   */
  protected[repositories] def collectionName: String

  /**
   * The mongodb's collection.
   *
   * @return The [[JSONCollection]] to run the actions.
   */
  protected[repositories] def collection: JSONCollection = MongoContext.db.collection[JSONCollection](collectionName)

  /**
   * Insert a [[T]].
   *
   * @param t The [[T]] to be inserted.
   * @return A [[Future]] of [[WriteResult]]
   */
  def insert(t: T): Future[WriteResult]

  /**
   * Update a [[T]].
   *
   * @param id The [[UUID]] of the [[T]] to be updated.
   * @param t  The [[T]] to be updated.
   * @return A [[Future]] of [[WriteResult]]
   */
  def update(id: UUID, t: T): Future[WriteResult]

  /**
   * Find [[T]]'s by a predicate.
   *
   * @param predicate The [[Predicate]] to the filter be based on.
   * @return A [[Future]] of [[List]] of a [[T]].
   */
  def findBy(predicate: Predicate): Future[List[T]]

  /**
   * Find only one [[T]].
   *
   * @param predicate The [[Predicate]] to the filter be based on.
   * @return A [[Future]] of [[Option]] of a [[T]].
   */
  def findOneBy(predicate: Predicate): Future[Option[T]]

  /**
   * Remove a [[T]].
   *
   * @param id The [[UUID]] of the [[T]].
   * @return A [[Future]] of [[WriteResult]]
   */
  def remove(id: UUID): Future[WriteResult]

  /**
   * Search [[T]]'s by a predicate.
   *
   * @param predicate The [[Predicate]] to the filter be based on.
   * @return A [[Future]] of [[Option]] of [[Page]] of a [[T]].
   */
  def search(predicate: Predicate): Future[Option[Page[T]]]

}

/** MongoContext */
object MongoContext {

  private val driver = new MongoDriver

  private val uri = MongoConnection.parseURI(Config.getString("mongodb.uri")).get

  private val connection = driver.connection(uri)

  private val dbs = connection.database(uri.authenticate.get.db)

  def db: DefaultDB = Await.result(dbs, 2.second)

}
