package com.gbm.mymangas.utils.json

import com.gbm.mymangas.models.Collection
import play.api.libs.functional.syntax._
import play.api.libs.json.Reads._
import play.api.libs.json._

/**
 * @author Gustavo Metzner on 10/13/15.
 */
object CollectionParser {

  private val MinLength = 3
  private val MaxLength = 30
  private val paramMaxLength = 50
  private val reads: Reads[Collection] = new Reads[Collection] {
    override def reads(json: JsValue): JsResult[Collection] = {
      val publisher = (json \ "publisher").as[String](minLength[String](MinLength) keepAnd maxLength[String](MaxLength))
      val name = (json \ "name").as[String](minLength[String](MinLength) keepAnd maxLength[String](MaxLength))
      val isComplete = (json \ "isComplete").as[Boolean]
      val searchParam = (json \ "searchParam").as[String](minLength[String](MinLength) keepAnd maxLength[String](paramMaxLength))
      JsSuccess(Collection(publisher = publisher, name = name, searchParam = searchParam, isComplete = isComplete))
    }
  }

  private val writes: Writes[Collection] = new Writes[Collection] {
    override def writes(c: Collection): JsValue = {
      Json.obj(
        "id" -> c.id,
        "publisher" -> c.publisher,
        "name" -> c.name,
        "searchParam" -> c.searchParam,
        "isComplete" -> c.isComplete,
        "createdAt" -> c.createdAt,
        "updatedAt" -> c.updatedAt
      )
    }
  }

  implicit val collectionFormatter = Format(reads, writes)
  implicit val collectionFormatterRepo = Json.format[Collection]
}
