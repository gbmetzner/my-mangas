package com.gbm.mymangas.models.filters

import java.util.UUID

import com.sksamuel.scrimage.Position
import org.joda.time.DateTime
import play.api.libs.json.{JsArray, JsObject, Json}

import scala.util.Random

/**
  * @author Gustavo Metzner on 10/13/15.
  */
case class MangaFilter(id: Option[UUID] = None,
                       collection: Option[String] = None,
                       number: Option[Int] = None,
                       from: Option[DateTime] = None,
                       to: Option[DateTime] = None,
                       randomSort: Boolean = false,
                       override val limit: Option[Int] = None,
                       override val skip: Option[Int] = None) extends Predicate {

  override def filter: JsObject = {

    val filter = (collection.map(c => Json.obj("collection" -> Json.obj("$regex" -> c, "$options" -> "i")))
      :: number.map(n => Json.obj("number" -> n))
      :: id.map(i => Json.obj("id" -> i))
      :: from.map(f => Json.obj("createdAt" -> Json.obj("$gte" -> f.getMillis)))
      :: to.map(t => Json.obj("createdAt" -> Json.obj("$lt" -> t.getMillis)))
      :: Nil).map(_.getOrElse(JsObject(Nil)))

    Json.obj("$and" -> JsArray(filter.toSeq))

  }

  override def sort: JsObject = {
    def random(): Int = if (Random.nextBoolean()) 1 else -1
    if (randomSort) Json.obj("doIHaveIt" -> 1, "collection" -> random(), "number" -> random())
    else Json.obj("doIHaveIt" -> 1, "collection" -> 1)
  }
}
