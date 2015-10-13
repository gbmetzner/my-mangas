package services

import models.Publisher
import models.filters.{Predicate, PublisherFilter}
import play.api.libs.json.Json
import play.modules.reactivemongo.json._
import play.modules.reactivemongo.json.collection.JSONCollection
import utils.json.PublisherParser.publisherFormatter
import utils.messages.{Error, Failed, Succeed}

import scala.concurrent.{ExecutionContext, Future}

/**
 * @author Gustavo Metzner on 10/10/15.
 */
class PublisherService {

  def insert(publisher: Publisher)(collection: JSONCollection)(implicit ec: ExecutionContext): Future[Either[Failed, Succeed]] = {
    findBy(PublisherFilter(name = publisher.name))(collection).flatMap {
      publishers => if (publishers.isEmpty) {
        (collection insert publisher).map {
          lastError => if (lastError.hasErrors) Left(Error(lastError.message)) else Right(Succeed("publisher.added"))
        }
      }
      else Future.successful(Left(Error("publisher.already.exists")))
    }
  }

  def findBy(predicate: Predicate)(collection: JSONCollection)(implicit ec: ExecutionContext): Future[List[Publisher]] = {
    collection.find(predicate.filter).
      sort(predicate.sort).
      cursor[Publisher]().collect[List]()
  }

}
