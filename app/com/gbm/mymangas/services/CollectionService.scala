package com.gbm.mymangas.services

import java.util.UUID

import com.gbm.mymangas.models.Collection
import com.gbm.mymangas.models.filters.{CollectionFilter, Predicate}
import com.gbm.mymangas.utils.messages.{Error, Failed, Succeed}
import org.joda.time.DateTime
import reactivemongo.api.commands.WriteResult

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Future, Promise}

/**
  * Defines a Collection Service.
  *
  * @author Gustavo Metzner on 12/8/15.
  */
class CollectionService extends Service[Collection] {

  def insert(coll: Collection)(f: Collection => Future[WriteResult])(g: Predicate => Future[Option[Collection]]): Future[Either[Failed, Succeed]] = {
    findOneBy(CollectionFilter(name = Some(coll.name)))(g).flatMap {
      colls =>
        if (colls.isEmpty) {
          f(coll).map {
            lastError =>
              if (lastError.writeErrors.nonEmpty) {
                logger error s"Error while persisting collection = $coll, errors = ${lastError.writeErrors.mkString}"
                Left(Error("error.general"))
              } else Right(Succeed("collection.added"))
          }
        } else Future.successful(Left(Error("collection.already.exists")))
    }
  }

  def update(id: UUID, coll: Collection)(f: (UUID, Collection) => Future[WriteResult])(g: Predicate => Future[List[Collection]])(h: (String, Boolean) => Future[Unit]): Future[Either[Failed, Succeed]] = {

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
            if (lastError.writeErrors.nonEmpty) {
              logger error s"Error while updating collection = $coll, errors = ${lastError.writeErrors.mkString}"
              promise.success(Left(Error("error.general")))
            } else {
              if (coll.isComplete) h(coll.name, coll.isComplete)
              promise.success(Right(Succeed("collection.updated")))
            }
        }
      case None => promise.success(Left(Error("collection.not.found")))
    }

    promise.future
  }

  override def remove(id: UUID)(f: (UUID) => Future[WriteResult])(g: (Predicate) => Future[Option[Collection]]): Future[Either[Failed, Succeed]] = {
    remove(id, "collection.removed", "collection.not.found")(f)(g)
  }
}
