package com.gbm.mymangas.services

import java.util.UUID

import com.gbm.mymangas.models.filters.{CollectionFilter, Predicate}
import com.gbm.mymangas.models.{Collection, Page}
import com.gbm.mymangas.utils.messages.{Error, Failed, Succeed}
import org.joda.time.DateTime
import reactivemongo.api.commands.WriteResult

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Future, Promise}

/**
  * @author Gustavo Metzner on 10/13/15.
  */
class CollectionService {
  //extends Service {

  def insert(coll: Collection)(f: Collection => Future[WriteResult])(g: Predicate => Future[List[Collection]]): Future[Either[Failed, Succeed]] = {
    findBy(CollectionFilter(name = Some(coll.name)))(g).flatMap {
      colls =>
        if (colls.isEmpty) {
          f(coll).map {
            lastError => if (lastError.hasErrors) Left(Error(lastError.message)) else Right(Succeed("collection.added"))
          }
        }
        else Future.successful(Left(Error("collection.already.exists")))
    }
  }

  def findBy(predicate: Predicate)(f: Predicate => Future[List[Collection]]): Future[List[Collection]] = {
    f(predicate)
  }

  def update(id: UUID, coll: Collection)(f: (UUID, Collection) => Future[WriteResult])
            (g: Predicate => Future[List[Collection]])(h: (String, Boolean) => Unit): Future[Either[Failed, Succeed]] = {

    val promise = Promise[Either[Failed, Succeed]]()

    val isAvailable: Future[Boolean] = findBy(CollectionFilter(name = Option(coll.name)))(g).map {
      collections => collections.filter(_.id != id).forall(p => p.name.toLowerCase != coll.name.toLowerCase)
    }

    isAvailable.foreach {
      available =>
        if (available) update()
        else promise.success(Left(Error("collection.already.exists")))
    }

    def update() = g(CollectionFilter(id = Option(id))).map(_.headOption).foreach {
      case Some(oldCollection) =>
        f(id, oldCollection.copy(publisher = coll.publisher, name = coll.name, updatedAt = DateTime.now())).map {
          lastError =>
            if (lastError.hasErrors) promise.success(Left(Error(lastError.message)))
            else {
              h(coll.name, coll.isComplete)
              promise.success(Right(Succeed("publisher.updated")))
            }
        }
      case None => promise.success(Left(Error("publisher.not.found")))
    }

    promise.future
  }

  def findOneBy(predicate: Predicate)(f: Predicate => Future[Option[Collection]]): Future[Option[Collection]] = {
    f(predicate)
  }

  def remove(id: UUID)(f: UUID => Future[WriteResult])(g: Predicate => Future[Option[Collection]]): Future[Either[Failed, Succeed]] = {
    findOneBy(CollectionFilter(id = Option(id)))(g).flatMap {
      case Some(_) => f(id).map {
        lastError =>
          if (lastError.hasErrors) Left(Error(lastError.message))
          else Right(Succeed("collection.removed"))
      }
      case None => Future.successful(Left(Error("collection.not.found")))
    }
  }

  def search(predicate: Predicate)(f: Predicate => Future[Option[Page[Collection]]]): Future[Option[Page[Collection]]] = {
    f(predicate)
  }
}
