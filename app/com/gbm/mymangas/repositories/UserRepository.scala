package com.gbm.mymangas.repositories

import java.util.UUID

import com.gbm.mymangas.models.filters.Predicate
import com.gbm.mymangas.models.{ Page, User }
import com.gbm.mymangas.utils.json.UserParser.userFormatterRepo
import play.modules.reactivemongo.json._
import reactivemongo.api.commands.WriteResult

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
 * Defines a User Repository.
 *
 * @author Gustavo Metzner on 12/8/15.
 */
class UserRepository extends Repository[User] {

  /**
   * The collection's name.
   *
   * @return The name of the collection.
   */
  override protected[repositories] def collectionName: String = "users"

  /**
   * This function is not implemented by this Repository.
   */
  override def insert(user: User): Future[WriteResult] = throw new NotImplementedError("Insert User not implemented.")

  /**
   * This function is not implemented by this Repository.
   */
  override def update(id: UUID, user: User): Future[WriteResult] = throw new NotImplementedError("Update User not implemented.")

  /**
   * This function is not implemented by this Repository.
   */
  override def findBy(predicate: Predicate): Future[scala.List[User]] = throw new NotImplementedError("Update User not implemented.")

  /**
   * Find only one [[User]].
   *
   * @param predicate The [[Predicate]] to the filter be based on.
   * @return A [[Future]] of [[Option]] of an [[User]].
   */
  override def findOneBy(predicate: Predicate): Future[Option[User]] = {
    collection.find(predicate.filter).one[User]
  }

  /**
   * This function is not implemented by this Repository.
   */
  override def remove(id: UUID): Future[WriteResult] = throw new NotImplementedError("Update User not implemented.")

  /**
   * This function is not implemented by this Repository.
   */
  override def search(predicate: Predicate): Future[Option[Page[User]]] = throw new NotImplementedError("Update User not implemented.")
}
