package com.gbm.mymangas.controllers

import java.util.UUID
import javax.inject.Inject

import com.gbm.mymangas.models.Publisher
import com.gbm.mymangas.models.filters.PublisherFilter
import com.gbm.mymangas.services.PublisherService
import com.gbm.mymangas.utils.json.PublisherParser.publisherFormatter
import play.api.libs.json.Json
import play.api.mvc.Action

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
 * @author Gustavo Metzner on 10/10/15.
 */
class PublisherController @Inject()(publisherService: PublisherService) extends BaseController {

  def create = Action.async(parse.json) {
    request =>

      logger debug s"Create a Publisher = $request"

      request.body.validate[Publisher].map {
        publisher => publisherService.insert(publisher).map {
          case Left(error) => BadRequest(Json.obj("msg" -> error.message))
          case Right(success) => Created(Json.obj("msg" -> success.message))
        }
      }.getOrElse(Future.successful(BadRequest("invalid json")))
  }

  def search = Action.async {
    request =>

      val name = request.getQueryString("name")
      val limit = request.getQueryString("limit").map(_.toInt)
      val skip = request.getQueryString("skip").map(_.toInt)

      logger debug s"Search by name = $name, limit = $limit and skip = $skip"

      publisherService.search(PublisherFilter(name = name, limit = limit, skip = skip)).map {
        case Some(page) => Ok(Json.obj("totalRecords" -> page.totalRecords, "items" -> Json.toJson(page.items)))
        case None => BadRequest("")
      }
  }

  def edit(id: UUID) = Action.async {
    logger debug s"Find by id = $id"

    publisherService.findOneBy(PublisherFilter(id = Some(id))).map {
      case Some(publisher) => Ok(Json.obj("publisher" -> Json.toJson(publisher)))
      case None => NotFound(Json.obj("msg" -> "publisher.not.found"))
    }
  }

  def update(id: UUID) = Action.async(parse.json) {
    request =>
      logger debug s"Update a Publisher = $request"

      request.body.validate[Publisher].map {
        publisher => publisherService.update(id, publisher).map {
          case Left(error) => BadRequest(Json.obj("msg" -> error.message))
          case Right(success) => Ok(Json.obj("msg" -> success.message))
        }
      }.getOrElse(Future.successful(BadRequest("invalid json")))
  }

  def remove(id: UUID) = Action.async {
    logger debug s"Removing id = $id"

    publisherService.remove(id).map {
      case Left(error) => BadRequest(Json.obj("msg" -> error.message))
      case Right(success) => Ok(Json.obj("msg" -> success.message))
    }
  }
}
