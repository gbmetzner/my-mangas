package utils.json

import models.{Publisher => P}
import play.api.libs.functional.syntax._
import play.api.libs.json.Reads._
import play.api.libs.json._

object Publisher {

  implicit val reads: Reads[P] = new Reads[P] {
    override def reads(json: JsValue): JsResult[P] = {
      val name = (json \ "name").as[String](minLength[String](3) keepAnd maxLength[String](30))
      JsSuccess(P(name = name))
    }
  }

  implicit val writes: Writes[P] = new Writes[P] {
    override def writes(p: P): JsValue = {
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