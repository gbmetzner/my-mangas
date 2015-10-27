package com.gbm.mymangas.services

import javax.inject.Inject

import com.gbm.mymangas.models.filters.{Predicate, PublisherFilter}
import com.gbm.mymangas.models.{Page, Publisher}
import com.gbm.mymangas.utils.json.PublisherParser.publisherFormatterJson
import com.gbm.mymangas.utils.messages.{Error, Failed, Succeed}
import play.api.libs.json.Json
import play.modules.reactivemongo.ReactiveMongoApi
import play.modules.reactivemongo.json._

import scala.concurrent.{ExecutionContext, Future}

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

  def findBy(predicate: Predicate)(implicit ec: ExecutionContext): Future[List[Publisher]] = {
    collection.find(predicate.filter).
      sort(predicate.sort).
      cursor[Publisher]().collect[List]()
  }

  def findOneBy(predicate: Predicate)(implicit ec: ExecutionContext): Future[Option[Publisher]] = {
    collection.find(predicate.filter).sort(predicate.sort).one[Publisher]
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
