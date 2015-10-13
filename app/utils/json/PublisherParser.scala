package utils.json

import models.Publisher
import play.api.libs.functional.syntax._
import play.api.libs.json.Reads._
import play.api.libs.json._

object PublisherParser {

  implicit val reads: Reads[Publisher] = new Reads[Publisher] {
    override def reads(json: JsValue): JsResult[Publisher] = {
      val name = (json \ "name").as[String](minLength[String](3) keepAnd maxLength[String](30))
      JsSuccess(Publisher(name = name))
    }
  }

  implicit val writes: Writes[Publisher] = new Writes[Publisher] {
    override def writes(p: Publisher): JsValue = {
      Json.obj(
        "id" -> p.id,
        "name" -> p.name,
        "createdAt" -> p.createdAt,
        "updatedAt" -> p.updatedAt
      )
    }
  }

  implicit val publisherFormatter = Format(reads, writes)
}