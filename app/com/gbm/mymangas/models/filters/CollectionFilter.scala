package com.gbm.mymangas.models.filters

import play.api.libs.json.{JsArray, JsObject, Json}

/**
 * @author Gustavo Metzner on 10/13/15.
 */
case class CollectionFilter(name: Option[String] = None,
                            isComplete: Option[Boolean] = None,
                            override val limit: Option[Int] = None,
                            override val skip: Option[Int] = None) extends Predicate {

  override def filter: JsObject = {
    val filter = (name.map {
      n => Json.obj("name" -> Json.obj("$regex" -> s".*$n*.", "$options" -> "i"))
    } :: isComplete.map(c => Json.obj("isComplete" -> c))
      :: Nil).map(_.getOrElse(JsObject(Nil)))

    Json.obj("$and" -> JsArray(filter.toSeq))
  }

  override def sort: JsObject = Json.obj("createdAt" -> -1)

}
