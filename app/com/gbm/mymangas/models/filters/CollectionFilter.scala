package com.gbm.mymangas.models.filters

import java.util.UUID

import play.api.libs.json.{JsArray, JsObject, Json}

/**
  * @author Gustavo Metzner on 10/13/15.
  */
case class CollectionFilter(id: Option[UUID] = None,
                            publisher: Option[String] = None,
                            name: Option[String] = None,
                            isComplete: Option[Boolean] = None,
                            override val limit: Option[Int] = None,
                            override val skip: Option[Int] = None) extends Predicate {

  override def filter: JsObject = {
    val filter = (
      id.map { i => Json.obj("id" -> id) } ::
        publisher.map { p => Json.obj("publisher" -> Json.obj("$regex" -> p, "$options" -> "i")) } ::
        name.map { n => Json.obj("name" -> Json.obj("$regex" -> n, "$options" -> "i")) } ::
        isComplete.map(c => Json.obj("isComplete" -> c)) ::
        Nil).map(_.getOrElse(JsObject(Nil)))

    Json.obj("$and" -> JsArray(filter.toSeq))
  }

  override def sort: JsObject = Json.obj("createdAt" -> -1)

}
