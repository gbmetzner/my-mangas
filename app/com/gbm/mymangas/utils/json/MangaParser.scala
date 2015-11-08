package com.gbm.mymangas.utils.json

import com.gbm.mymangas.models.Manga
import com.gbm.mymangas.models.filters.{Predicate, MangaFilter}
import play.api.libs.functional.syntax._
import play.api.libs.json.Reads._
import play.api.libs.json._
import play.api.mvc.{AnyContent, Request}
import com.gbm.mymangas.utils.UUID._

/**
  * @author Gustavo Metzner on 10/13/15.
  */
object MangaParser {

  private val reads: Reads[Manga] = new Reads[Manga] {
    override def reads(json: JsValue): JsResult[Manga] = {
      val collection = (json \ "collection").as[String](minLength[String](3) keepAnd maxLength[String](30))
      val name = (json \ "name").as[String](minLength[String](3) keepAnd maxLength[String](30))
      val number = (json \ "number").as[Int]
      val doIHaveIt = (json \ "doIHaveIt").as[Boolean]
      JsSuccess(Manga(name = name, collection = collection, number = number, doIHaveIt = doIHaveIt))
    }
  }

  private val writes: Writes[Manga] = new Writes[Manga] {
    override def writes(m: Manga): JsValue = {
      Json.obj(
        "id" -> m.id,
        "collection" -> m.collection,
        "name" -> m.name,
        "number" -> m.number,
        "publicLink" -> m.publicLink,
        "doIHaveIt" -> m.doIHaveIt,
        "createdAt" -> m.createdAt,
        "updatedAt" -> m.updatedAt
      )
    }
  }

  implicit val mangaFormatterController = Format(reads, writes)

  implicit val mangaFormatterService = Json.format[Manga]

  def queryString2Predicate(request: Request[AnyContent]): Predicate = {
    val id = request.getQueryString("id").map(fromString)
    val collection = request.getQueryString("collection")
    val name = request.getQueryString("name")
    val number = request.getQueryString("number").map(_.toInt)
    val limit = request.getQueryString("limit").map(_.toInt)
    val skip = request.getQueryString("skip").map(_.toInt)
    MangaFilter(id = id, collection = collection, name = name, number = number, limit = limit, skip = skip)
  }

}
