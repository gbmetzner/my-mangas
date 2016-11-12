package com.gbm.mymangas.controllers

import java.util.UUID
import javax.inject.Inject

import com.gbm.mymangas.models.Publisher
import com.gbm.mymangas.models.filters.PublisherFilter
import com.gbm.mymangas.registries.PublisherComponent
import com.gbm.mymangas.repositories.PublisherRepository
import com.gbm.mymangas.services.PublisherService
import com.gbm.mymangas.utils.json.PublisherParser.{ publisherFormatter, queryString2Predicate }
import play.api.cache.CacheApi
import play.api.i18n.MessagesApi
import play.api.libs.json.{ JsValue, Json }
import play.api.mvc.{ Action, AnyContent }

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
 * Controller for Publisher actions.
 *
 * @author Gustavo Metzner on 10/10/15.
 */
class PublisherController @Inject() (
    val messagesApi: MessagesApi,
    val cacheApi: CacheApi,
    val publisherComponent: PublisherComponent
) extends BaseController {

  private val publisherService: PublisherService = publisherComponent.publisherService
  private val publisherRepository: PublisherRepository = publisherComponent.publisherRepository

  /**
   * Creates a new [[Publisher]] based on the json content received.
   *
   * @return Whether the [[Publisher]] was created or not.
   */
  def createPublisher: Action[JsValue] = hasTokenAsync(parse.json) { _ => _ => request =>

    logger debug s"Create a Publisher = $request"

    request.body.validate[Publisher].map {
      publisher =>
        publisherService.insert(publisher)(publisherRepository.insert)(publisherRepository.findOneBy).map {
          case Left(error) =>
            BadRequest(Json.obj("msg" -> withMessage(error.message)))
          case Right(success) =>
            Created(Json.obj("msg" -> withMessage(success.message)))
        }
    }.getOrElse(Future(BadRequest(Json.obj("msg" -> withMessage("error.invalid.json")))))
  }

  def searchPublishers: Action[AnyContent] = Action.async {
    request =>

      val predicate = queryString2Predicate(request)

      logger debug s"Search by predicate = $predicate"

      publisherService.search(predicate)(publisherRepository.search).map {
        case Some(page) => Ok(Json.obj("totalRecords" -> page.totalRecords, "items" -> Json.toJson(page.items)))
        case None => BadRequest("")
      }
  }

  def editPublisher(id: UUID): Action[AnyContent] = hasTokenAsync() { _ => _ => request =>

    logger debug s"Find by id = $id"

    publisherService.findOneBy(PublisherFilter(id = Some(id)))(publisherRepository.findOneBy).map {
      case Some(publisher) => Ok(Json.obj("publisher" -> Json.toJson(publisher)))
      case None => NotFound(Json.obj("msg" -> withMessage("publisher.not.found")))
    }
  }

  def updatePublisher(id: UUID): Action[JsValue] = hasTokenAsync(parse.json) { _ => _ => request =>

    logger debug s"Update a Publisher = $request"

    request.body.validate[Publisher].map {
      publisher =>
        publisherService.update(id, publisher)(publisherRepository.update)(publisherRepository.findBy).map {
          case Left(error) => BadRequest(Json.obj("msg" -> withMessage(error.message)))
          case Right(success) => Ok(Json.obj("msg" -> withMessage(success.message)))
        }
    } getOrElse Future.successful(BadRequest(withMessage("error.invalid.json")))
  }

  def removePublisher(id: UUID): Action[AnyContent] = hasTokenAsync() { _ => _ => request =>

    logger debug s"Removing id = $id"

    publisherService.remove(id)(publisherRepository.remove)(publisherRepository.findOneBy).map {
      case Left(error) => BadRequest(Json.obj("msg" -> withMessage(error.message)))
      case Right(success) => Ok(Json.obj("msg" -> withMessage(success.message)))
    }
  }
}
