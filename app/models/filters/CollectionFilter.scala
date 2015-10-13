package models.filters

import play.api.libs.json.{JsObject, Json}

/**
 * @author Gustavo Metzner on 10/13/15.
 */
object CollectionFilter {

  def apply(name: String = ""): Predicate = new Predicate {
    override def filter: JsObject = Json.obj(
      "name" -> Json.obj(
        "$regex" -> s".*$name*.", "$options" -> "i"
      )
    )

    override def sort: JsObject = Json.obj(
      "createdAt" -> -1
    )
  }
}
