package com.gbm.mymangas.services

import java.util.UUID
import javax.inject.Inject

import com.gbm.mymangas.models.filters.{CollectionFilter, Predicate}
import com.gbm.mymangas.models.{Collection, Page}
import com.gbm.mymangas.utils.json.CollectionParser.collectionFormatterService
import com.gbm.mymangas.utils.messages.{Error, Failed, Succeed}
import org.joda.time.DateTime
import play.api.libs.json.Json
import play.modules.reactivemongo.ReactiveMongoApi
import play.modules.reactivemongo.json._

import scala.concurrent.{ExecutionContext, Future, Promise}

/**
  * @author Gustavo Metzner on 10/13/15.
  */
class CollectionService @Inject()(mangaService: MangaService,
                                  val reactiveMongoApi: ReactiveMongoApi) extends Service {

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

  def update(id: UUID, coll: Collection)(implicit ec: ExecutionContext): Future[Either[Failed, Succeed]] = {

    val promise = Promise[Either[Failed, Succeed]]()

    val isAvailable: Future[Boolean] = findBy(CollectionFilter(name = Option(coll.name))).map {
      collections => collections.filter(_.id != id).forall(p => p.name.toLowerCase != coll.name.toLowerCase)
    }

    isAvailable.foreach { available =>
      if (available) update()
      else promise.success(Left(Error("co.already.exists")))
    }

    def update() = findOneBy(CollectionFilter(id = Option(id))).foreach {
      case Some(oldCollection) =>
        collection.update(Json.obj("id" -> id), oldCollection.copy(publisher = coll.publisher, name = coll.name, updatedAt = DateTime.now())).map {
          lastError =>
            if (lastError.hasErrors) promise.success(Left(Error(lastError.message)))
            else {
              mangaService.updateComplete(coll.name, coll.isComplete)
              promise.success(Right(Succeed("publisher.updated")))
            }
        }
      case None => promise.success(Left(Error("publisher.not.found")))
    }

    promise.future
  }

  def findOneBy(predicate: Predicate)(implicit ec: ExecutionContext): Future[Option[Collection]] = {
    collection.find(predicate.filter).sort(predicate.sort).one[Collection]
  }

  def remove(id: UUID)(implicit ec: ExecutionContext): Future[Either[Failed, Succeed]] = {
    findOneBy(CollectionFilter(id = Option(id))).flatMap {
      case Some(_) => collection.remove(Json.obj("id" -> id)).map {
        lastError =>
          if (lastError.hasErrors) Left(Error(lastError.message))
          else Right(Succeed("collection.removed"))
      }
      case None => Future.successful(Left(Error("collection.not.found")))
    }
  }

  def search(predicate: Predicate)(implicit ec: ExecutionContext): Future[Option[Page[Collection]]] = {
    val totalRecordsFuture = collection.find(predicate.filter).cursor[Collection]().collect[List]().map {
      records => records.size
    }

    val itemsFuture = collection.find(predicate.filter)
      .options(predicate.queryOpts)
      .sort(predicate.sort).cursor[Collection]().collect[List](predicate.queryOpts.batchSizeN)

    for {
      totalRecords <- totalRecordsFuture
      items <- itemsFuture
    } yield Option(Page(totalRecords = totalRecords, items = items))
  }
}
