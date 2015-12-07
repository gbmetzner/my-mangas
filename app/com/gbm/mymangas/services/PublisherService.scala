package com.gbm.mymangas.services

import java.util.UUID

import com.gbm.mymangas.models.filters.{Predicate, PublisherFilter}
import com.gbm.mymangas.models.{Page, Publisher}
import com.gbm.mymangas.utils.messages.{Error, Failed, Succeed}
import org.joda.time.DateTime
import reactivemongo.api.commands.WriteResult

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Future, Promise}

/**
  * @author Gustavo Metzner on 10/10/15.
  */
class PublisherService {

  def insert(publisher: Publisher)(f: Publisher => Future[WriteResult])(g: Predicate => Future[List[Publisher]]): Future[Either[Failed, Succeed]] = {
    findBy(PublisherFilter(name = Some(publisher.name)))(g).flatMap {
      publishers => if (publishers.isEmpty) {
        f(publisher).map {
          lastError => if (lastError.hasErrors) Left(Error(lastError.message)) else Right(Succeed("publisher.added"))
        }
      }
      else Future.successful(Left(Error("publisher.already.exists")))
    }
  }

  def update(id: UUID, publisher: Publisher)(f: (UUID, Publisher) => Future[WriteResult])(g: Predicate => Future[List[Publisher]]): Future[Either[Failed, Succeed]] = {

    val promise = Promise[Either[Failed, Succeed]]()

    val isAvailable: Future[Boolean] = findBy(PublisherFilter(name = Option(publisher.name)))(g).map {
      publishers => publishers.filter(_.id != id).forall(p => p.name.toLowerCase != publisher.name.toLowerCase)
    }

    isAvailable.foreach { available =>
      if (available) update()
      else promise.success(Left(Error("publisher.already.exists")))
    }

    def update() = findBy(PublisherFilter(id = Option(id)))(g).map(_.headOption).foreach {
      case Some(oldPublisher) =>
        f(id, oldPublisher.copy(name = publisher.name, updatedAt = DateTime.now())).map {
          lastError =>
            if (lastError.hasErrors) promise.success(Left(Error(lastError.message)))
            else promise.success(Right(Succeed("publisher.updated")))
        }
      case None => promise.success(Left(Error("publisher.not.found")))
    }

    promise.future
  }

  def findBy(predicate: Predicate)(f: Predicate => Future[List[Publisher]]): Future[List[Publisher]] = {
    f(predicate)
  }

  def findOneBy(predicate: Predicate)(f: Predicate => Future[Option[Publisher]]): Future[Option[Publisher]] = {
    f(predicate)
  }

  def remove(id: UUID)(f: UUID => Future[WriteResult])(g: Predicate => Future[Option[Publisher]]): Future[Either[Failed, Succeed]] = {
    findOneBy(PublisherFilter(id = Option(id)))(g).flatMap {
      case Some(_) => f(id).map {
        lastError =>
          if (lastError.hasErrors) Left(Error(lastError.message))
          else Right(Succeed("publisher.removed"))
      }
      case None => Future.successful(Left(Error("publisher.not.found")))
    }
  }

  def search(predicate: Predicate)(f: Predicate => Future[Option[Page[Publisher]]]): Future[Option[Page[Publisher]]] = {
    f(predicate)
  }
}
