package com.gbm.mymangas.services

import javax.inject.Inject
import com.gbm.mymangas.models.Collection
import com.gbm.mymangas.models.filters.{Predicate, CollectionFilter}
import play.modules.reactivemongo.ReactiveMongoApi
import play.modules.reactivemongo.json._
import com.gbm.mymangas.utils.json.CollectionParser.collectionFormatterService
import com.gbm.mymangas.utils.messages.{Error, Failed, Succeed}

import scala.concurrent.{ExecutionContext, Future}

/**
 * @author Gustavo Metzner on 10/13/15.
 */
class CollectionService @Inject()(val reactiveMongoApi: ReactiveMongoApi) extends Service {

  override protected[services] val collectionName: String = "collections"

  def insert(coll: Collection)(implicit ec: ExecutionContext): Future[Either[Failed, Succeed]] = {
    findBy(CollectionFilter(name = Some(coll.name))).flatMap {
      colls => if (colls.isEmpty) {
        (collection insert coll).map {
          lastError => if (lastError.hasErrors) Left(Error(lastError.message)) else Right(Succeed("collection.added"))
        }
      }
      else Future.successful(Left(Error("collection.already.exists")))
    }
  }

  def findBy(predicate: Predicate)(implicit ec: ExecutionContext): Future[List[Collection]] = {
    collection.find(predicate.filter).
      sort(predicate.sort).
      cursor[Collection]().collect[List]()
  }
}
