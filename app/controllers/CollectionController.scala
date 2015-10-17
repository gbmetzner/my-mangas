package controllers

import javax.inject.Inject

import models.Collection
import models.filters.CollectionFilter
import play.api.libs.json.Json
import play.api.mvc.Action
import services.CollectionService
import utils.json.CollectionParser.collectionFormatter

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
 * @author Gustavo Metzner on 10/13/15.
 */
class CollectionController @Inject()(collectionService: CollectionService) extends BaseController {

  def create = Action.async(parse.json) {
    request =>

      logger debug s"Create a Collection = $request"

      request.body.validate[Collection].map {
        coll => collectionService.insert(coll).map {
          case Left(error) => BadRequest(error.message)
          case Right(success) => Created(success.message)
        }
      }.getOrElse(Future.successful(BadRequest("invalid json")))
  }

  def findByName(name: String) = Action.async {

    logger debug s"Find by name = $name"

    collectionService.findBy(CollectionFilter(name = Some(name))).map {
      collections => Ok(Json.toJson(collections))
    }
  }

}
