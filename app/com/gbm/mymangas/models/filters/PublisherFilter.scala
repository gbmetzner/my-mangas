package com.gbm.mymangas.models.filters

import java.util.UUID

import play.api.libs.json.{JsArray, JsObject, Json}

/**
 * @author Gustavo Metzner on 10/12/15.
 */
case class PublisherFilter(id: Option[UUID] = None,
                           name: Option[String] = None,
                           override val limit: Option[Int] = None,
                           override val skip: Option[Int] = None) extends Predicate {

  override def filter: JsObject = {
    val filter = (name.map(n => Json.obj("name" -> Json.obj(
      "$regex" -> n, "$options" -> "i"
    ))) :: id.map(i => Json.obj("id" -> i
    )) :: Nil).map(_.getOrElse(JsObject(Nil)))

    Json.obj("$and" -> JsArray(filter))
  }

  override def sort: JsObject = Json.obj(
    "createdAt" -> -1
  )
}
