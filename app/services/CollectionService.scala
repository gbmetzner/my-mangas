package services

import models.Collection
import models.filters.{Predicate, PublisherFilter}
import play.modules.reactivemongo.json._
import play.modules.reactivemongo.json.collection.JSONCollection
import utils.json.CollectionParser.collectionFormatterJson
import utils.messages.{Error, Failed, Succeed}

import scala.concurrent.{ExecutionContext, Future}

/**
 * @author Gustavo Metzner on 10/13/15.
 */
class CollectionService {

  def insert(coll: Collection)(collection: JSONCollection)(implicit ec: ExecutionContext): Future[Either[Failed, Succeed]] = {
    findBy(PublisherFilter(name = Some(coll.name)))(collection).flatMap {
      publishers => if (publishers.isEmpty) {
        (collection insert coll).map {
          lastError => if (lastError.hasErrors) Left(Error(lastError.message)) else Right(Succeed("collection.added"))
        }
      }
      else Future.successful(Left(Error("collection.already.exists")))
    }
  }

  def findBy(predicate: Predicate)(collection: JSONCollection)(implicit ec: ExecutionContext): Future[List[Collection]] = {
    collection.find(predicate.filter).
      sort(predicate.sort).
      cursor[Collection]().collect[List]()
  }
}
