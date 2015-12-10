package com.gbm.mymangas.controllers

import java.util.UUID
import javax.inject.Inject

import com.gbm.mymangas.models.Publisher
import com.gbm.mymangas.models.filters.PublisherFilter
import com.gbm.mymangas.registries.PublisherComponentRegistry
import com.gbm.mymangas.repositories.PublisherRepositoryComponent
import com.gbm.mymangas.services.PublisherServiceComponent
import com.gbm.mymangas.utils.json.PublisherParser.publisherFormatter
import play.api.i18n.MessagesApi
import play.api.libs.json.Json
import play.api.mvc.Action

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * @author Gustavo Metzner on 10/10/15.
  */
class PublisherController @Inject()(val messagesApi: MessagesApi)
  extends BaseController with PublisherComponentRegistry {
  requires: PublisherServiceComponent with PublisherRepositoryComponent =>


  def createPublisher = HasTokenAsync(parse.json) {
    _ => _ => request =>

      logger debug s"Create a Publisher = $request"

      request.body.validate[Publisher].map {
        publisher => publisherService.insert(publisher)(publisherRepository.insert)(publisherRepository.findBy).map {
          case Left(error) => BadRequest(Json.obj("msg" -> error.message))
          case Right(success) => Created(Json.obj("msg" -> success.message))
        }
      }.getOrElse(Future.successful(BadRequest("invalid json")))
  }

  def searchPublishers = Action.async {
    request =>

      val name = request.getQueryString("name")
      val limit = request.getQueryString("limit").map(_.toInt)
      val skip = request.getQueryString("skip").map(_.toInt)

      logger debug s"Search by name = $name, limit = $limit and skip = $skip"

      publisherService.search(PublisherFilter(name = name, limit = limit, skip = skip))(publisherRepository.search).map {
        case Some(page) => Ok(Json.obj("totalRecords" -> page.totalRecords, "items" -> Json.toJson(page.items)))
        case None => BadRequest("")
      }
  }

  def editPublisher(id: UUID) = HasTokenAsync() {
    _ => _ => request =>

      logger debug s"Find by id = $id"

      publisherService.findOneBy(PublisherFilter(id = Some(id)))(publisherRepository.findOneBy).map {
        case Some(publisher) => Ok(Json.obj("publisher" -> Json.toJson(publisher)))
        case None => NotFound(Json.obj("msg" -> "publisher.not.found"))
      }
  }

  def updatePublisher(id: UUID) = HasTokenAsync(parse.json) {
    _ => _ => request =>

      logger debug s"Update a Publisher = $request"

      request.body.validate[Publisher].map {
        publisher => publisherService.update(id, publisher)(publisherRepository.update)(publisherRepository.findBy).map {
          case Left(error) => BadRequest(Json.obj("msg" -> error.message))
          case Right(success) => Ok(Json.obj("msg" -> success.message))
        }
      }.getOrElse(Future.successful(BadRequest("invalid json")))
  }

  def removePublisher(id: UUID) = HasTokenAsync() {
    _ => _ => request =>

      logger debug s"Removing id = $id"

      publisherService.remove(id)(publisherRepository.remove)(publisherRepository.findOneBy).map {
        case Left(error) => BadRequest(Json.obj("msg" -> error.message))
        case Right(success) => Ok(Json.obj("msg" -> success.message))
      }
  }
}
