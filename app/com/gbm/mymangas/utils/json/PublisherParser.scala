package com.gbm.mymangas.utils.json

import com.gbm.mymangas.models.Publisher
import com.gbm.mymangas.models.filters.{PublisherFilter, MangaFilter, Predicate}
import com.gbm.mymangas.utils.UUID._
import play.api.libs.functional.syntax._
import play.api.libs.json.Reads._
import play.api.libs.json._
import play.api.mvc.{AnyContent, Request}

object PublisherParser {

  private val reads: Reads[Publisher] = new Reads[Publisher] {
    override def reads(json: JsValue): JsResult[Publisher] = {
      val name = (json \ "name").as[String](minLength[String](3) keepAnd maxLength[String](30))
      JsSuccess(Publisher(name = name))
    }
  }

  private val writes: Writes[Publisher] = new Writes[Publisher] {
    override def writes(p: Publisher): JsValue = {
      Json.obj(
        "id" -> p.id,
        "name" -> p.name,
        "createdAt" -> p.createdAt,
        "updatedAt" -> p.updatedAt
      )
    }
  }

  def queryString2Predicate(request: Request[AnyContent]): Predicate = {
    val name = request.getQueryString("name")
    val limit = request.getQueryString("limit").map(_.toInt)
    val skip = request.getQueryString("skip").map(_.toInt)
    PublisherFilter(name = name, limit = limit, skip = skip)
  }

  implicit val publisherFormatter = Format(reads, writes)
  implicit val publisherFormatterRepo = Json.format[Publisher]
}