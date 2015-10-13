package utils.json

import models.Collection
import play.api.libs.functional.syntax._
import play.api.libs.json.Reads._
import play.api.libs.json._

/**
 * Created by gbmetzner on 10/13/15.
 */
object CollectionParser {

  implicit val reads: Reads[Collection] = new Reads[Collection] {
    override def reads(json: JsValue): JsResult[Collection] = {
      val name = (json \ "name").as[String](minLength[String](3) keepAnd maxLength[String](30))
      JsSuccess(Collection(name = name))
    }
  }

  implicit val writes: Writes[Collection] = new Writes[Collection] {
    override def writes(c: Collection): JsValue = {
      Json.obj(
        "id" -> c.id,
        "name" -> c.name,
        "createdAt" -> c.createdAt,
        "updatedAt" -> c.updatedAt
      )
    }
  }

  implicit val collectionFormatter = Format(reads, writes)
}
