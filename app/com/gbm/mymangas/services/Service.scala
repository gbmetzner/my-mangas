package com.gbm.mymangas.services

import java.util.UUID

import com.gbm.mymangas.models.Page
import com.gbm.mymangas.models.filters.Predicate
import com.gbm.mymangas.utils.messages.{ Error, Failed, Succeed }
import com.typesafe.scalalogging.LazyLogging
import play.api.libs.json.{ JsObject, Json }
import reactivemongo.api.commands.WriteResult

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
 * @author Gustavo Metzner on 10/16/15.
 */
trait Service[T] extends LazyLogging {

  def findBy(predicate: Predicate)(f: Predicate => Future[List[T]]): Future[List[T]] = {
    logger debug s"Finding by $predicate"
    f(predicate)
  }

  def findOneBy(predicate: Predicate)(f: Predicate => Future[Option[T]]): Future[Option[T]] = {
    logger debug s"Finding one by $predicate"
    f(predicate)
  }

  def search(predicate: Predicate)(f: Predicate => Future[Option[Page[T]]]): Future[Option[Page[T]]] = {
    logger debug s"searching by $predicate"
    f(predicate)
  }

  def remove(id: UUID)(f: UUID => Future[WriteResult])(g: Predicate => Future[Option[T]]): Future[Either[Failed, Succeed]]

  protected[services] def remove(id: UUID, removeMsg: String, notFoundMsg: String)(f: UUID => Future[WriteResult])(g: Predicate => Future[Option[T]]): Future[Either[Failed, Succeed]] = {
    logger debug s"Removing id $id"

    val predicate = new Predicate {
      override def filter: JsObject = Json.obj("id" -> id)

      override def sort: JsObject = Json.obj("doIHaveIt" -> -1)

      override val limit: Option[Int] = None
      override val skip: Option[Int] = None
    }

    findOneBy(predicate)(g).flatMap {
      case Some(result) => f(id).map {
        lastError =>
          if (lastError.hasErrors) {
            logger error (s"Error while removing = $result", lastError.message)
            Left(Error("error.general"))
          } else Right(Succeed(removeMsg))
      }
      case None => Future.successful(Left(Error(notFoundMsg)))
    }
  }
}
