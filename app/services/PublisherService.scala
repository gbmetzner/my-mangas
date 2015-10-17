package services

import javax.inject.Inject

import models.Publisher
import models.filters.{Predicate, PublisherFilter}
import play.modules.reactivemongo.ReactiveMongoApi
import play.modules.reactivemongo.json._
import utils.json.PublisherParser.publisherFormatterJson
import utils.messages.{Error, Failed, Succeed}

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

}
