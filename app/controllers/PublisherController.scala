package controllers

import javax.inject.Inject

import models.Publisher
import models.filters.PublisherFilter
import play.api.libs.json.Json
import play.api.mvc.Action
import play.modules.reactivemongo.ReactiveMongoApi
import services.PublisherService
import utils.json.Publisher.publisherFormatter

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
 * @author Gustavo Metzner on 10/10/15.
 */
class PublisherController @Inject()(publisherService: PublisherService,
                                    val reactiveMongoApi: ReactiveMongoApi) extends BaseController {

  override protected[controllers] val collectionName: String = "publishers"

  def create = Action.async(parse.json) {
    request =>

      logger debug s"Create a Publisher = $request"

      request.body.validate[Publisher].map {
        publisher => publisherService.insert(publisher)(collection).map {
          case Left(error) => BadRequest(error.message)
          case Right(success) => Created(success.message)
        }
      }.getOrElse(Future.successful(BadRequest("invalid json")))
  }

  def findByName(name: String) = Action.async {

    logger debug s"Find by name = $name"

    publisherService.findBy(PublisherFilter(name = name))(collection).map {
      publishers => Ok(Json.toJson(publishers))
    }
  }

}
