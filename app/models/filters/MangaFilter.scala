package models.filters

import java.util.UUID

import play.api.libs.json.{JsArray, JsObject, Json}

/**
 * @author Gustavo Metzner on 10/13/15.
 */
case class MangaFilter(id: Option[UUID] = None,
                       collection: Option[String] = None,
                       name: Option[String] = None,
                       number: Option[String] = None,
                       override val limit: Option[Int] = None,
                       override val skip: Option[Int] = None) extends Predicate {

  override def filter: JsObject = {

    val filter = (collection.map(c => Json.obj("collection" -> Json.obj("$regex" -> s".*$c*.", "$options" -> "i")))
      :: name.map(n => Json.obj("name" -> Json.obj("$regex" -> s".*$n*.", "$options" -> "i")))
      :: number.map(n => Json.obj("number" -> n))
      :: id.map(i => Json.obj("id" -> i))
      :: Nil).map(_.getOrElse(JsObject(Nil)))

    Json.obj("$and" -> JsArray(filter.toSeq))

  }

  override def sort: JsObject = Json.obj("createdAt" -> -1)
}
