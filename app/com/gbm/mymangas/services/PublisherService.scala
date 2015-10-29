package com.gbm.mymangas.services

import java.util.UUID
import javax.inject.Inject

import com.gbm.mymangas.models.filters.{Predicate, PublisherFilter}
import com.gbm.mymangas.models.{Page, Publisher}
import com.gbm.mymangas.utils.json.PublisherParser.publisherFormatterJson
import com.gbm.mymangas.utils.messages.{Error, Failed, Succeed}
import org.joda.time.DateTime
import play.api.libs.json.Json
import play.modules.reactivemongo.ReactiveMongoApi
import play.modules.reactivemongo.json._

import scala.concurrent.{ExecutionContext, Future, Promise}

/**
 * @author Gustavo Metzner on 10/10/15.
 */
class PublisherService @Inject()(val reactiveMongoApi: ReactiveMongoApi) extends Service {

  override protected[services] val collectionName: String = "publishers"

  def insert(publisher: Publisher)(implicit ec: ExecutionContext): Future[Either[Failed, Succeed]] = {
    findBy(PublisherFilter(name = Some(publisher.name))).flatMap {
      publishers => if (publishers.isEmpty) {
        (collection insert publisher).map {
          lastError => if (lastError.hasErrors) Left(Error(lastError.message)) else Right(Succeed("publisher.added"))
        }
      }
      else Future.successful(Left(Error("publisher.already.exists")))
    }
  }

  def update(id: UUID, publisher: Publisher)(implicit ec: ExecutionContext): Future[Either[Failed, Succeed]] = {

    val promise = Promise[Either[Failed, Succeed]]()

    val isAvailable: Future[Boolean] = findBy(PublisherFilter(name = Option(publisher.name))).map {
      publishers => publishers.filter(_.id != id).forall(p => p.name.toLowerCase != publisher.name.toLowerCase)
    }

    isAvailable.foreach { available =>
      if (available) update()
      else promise.success(Left(Error("publisher.already.exists")))
    }

    def update() = findOneBy(PublisherFilter(id = Option(id))).foreach {
      case Some(oldPublisher) => {
        collection.update(Json.obj("id" -> id), oldPublisher.copy(name = publisher.name, updatedAt = DateTime.now())).map {
          lastError =>
            if (lastError.hasErrors) promise.success(Left(Error(lastError.message)))
            else promise.success(Right(Succeed("publisher.updated")))
        }
      }
      case None => promise.success(Left(Error("publisher.not.found")))
    }

    promise.future
  }

  def findBy(predicate: Predicate)(implicit ec: ExecutionContext): Future[List[Publisher]] = {
    collection.find(predicate.filter).
      sort(predicate.sort).
      cursor[Publisher]().collect[List]()
  }

  def findOneBy(predicate: Predicate)(implicit ec: ExecutionContext): Future[Option[Publisher]] = {
    collection.find(predicate.filter).sort(predicate.sort).one[Publisher]
  }

  def remove(id: UUID)(implicit ec: ExecutionContext): Future[Either[Failed, Succeed]] = {
    findOneBy(PublisherFilter(id = Option(id))).flatMap {
      case Some(_) => collection.remove(Json.obj("id" -> id)).map {
        lastError =>
          if (lastError.hasErrors) Left(Error(lastError.message))
          else Right(Succeed("publisher.removed"))
      }
      case None => Future.successful(Left(Error("publisher.not.found")))
    }
  }

  def search(predicate: Predicate)(implicit ec: ExecutionContext): Future[Option[Page[Publisher]]] = {
    val totalRecordsFuture = collection.find(predicate.filter).cursor[Publisher]().collect[List]().map {
      records => records.size
    }
    val itemsFuture = collection.find(predicate.filter)
      .options(predicate.queryOpts)
      .sort(Json.obj("createdAt" -> -1)).cursor[Publisher]().collect[List]()

    for {
      totalRecords <- totalRecordsFuture
      items <- itemsFuture
    } yield Option(Page(totalRecords = totalRecords, items = items))
  }
}
