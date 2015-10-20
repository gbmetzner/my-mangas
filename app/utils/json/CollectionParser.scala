package utils.json

import models.Collection
import play.api.libs.functional.syntax._
import play.api.libs.json.Reads._
import play.api.libs.json._

/**
 * @author Gustavo Metzner on 10/13/15.
 */
object CollectionParser {

  private val reads: Reads[Collection] = new Reads[Collection] {
    override def reads(json: JsValue): JsResult[Collection] = {
      val publisher = (json \ "publisher").as[String](minLength[String](3) keepAnd maxLength[String](30))
      val name = (json \ "name").as[String](minLength[String](3) keepAnd maxLength[String](30))
      val baseURL = (json \ "baseURL").as[String](minLength[String](15) keepAnd maxLength[String](50))
      JsSuccess(Collection(publisher = publisher, name = name, baseURL = baseURL))
    }
  }

  private val writes: Writes[Collection] = new Writes[Collection] {
    override def writes(c: Collection): JsValue = {
      Json.obj(
        "id" -> c.id,
        "publisher" -> c.publisher,
        "name" -> c.name,
        "baseURL" -> c.baseURL,
        "isComplete" -> c.isComplete,
        "createdAt" -> c.createdAt,
        "updatedAt" -> c.updatedAt
      )
    }
  }

  implicit val collectionFormatter = Format(reads, writes)
  implicit val collectionFormatterService = Json.format[Collection]
}
