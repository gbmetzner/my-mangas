package com.gbm.mymangas.controllers

import java.util.UUID
import javax.inject.Inject

import com.gbm.mymangas.models.Collection
import com.gbm.mymangas.models.filters.CollectionFilter
import com.gbm.mymangas.repositories.CollectionRepository
import com.gbm.mymangas.services.{CollectionService, MangaService}
import com.gbm.mymangas.utils.json.CollectionParser.collectionFormatter
import play.api.i18n.MessagesApi
import play.api.libs.json.Json
import play.api.mvc.Action

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * @author Gustavo Metzner on 10/13/15.
  */
class CollectionController @Inject()(collectionService: CollectionService,
                                     mangaService: MangaService,
                                     collectionRepository: CollectionRepository,
                                     val messagesApi: MessagesApi) extends BaseController {

  import collectionRepository._
  import mangaService.completeUpdate

  def createCollection = HasTokenAsync(parse.json) {
    _ => _ => request =>

      logger info s"Create a Collection = $request"

      request.body.validate[Collection].map {
        coll => collectionService.insert(coll)(insert)(findBy).map {
          case Left(error) => BadRequest(Json.obj("msg" -> error.message))
          case Right(success) => Created(Json.obj("msg" -> success.message))
        }
      }.getOrElse(Future.successful(BadRequest("invalid json")))
  }

  def searchCollections = Action.async {
    request =>

      val publisher = request.getQueryString("publisher")
      val name = request.getQueryString("name")
      val limit = request.getQueryString("limit").map(_.toInt)
      val skip = request.getQueryString("skip").map(_.toInt)

      logger debug s"Search by publisher = $publisher, name = $name, limit = $limit and skip = $skip for request = $request"

      collectionService.search(CollectionFilter(name = name, limit = limit, skip = skip))(search).map {
        case Some(page) => Ok(Json.obj("totalRecords" -> page.totalRecords, "items" -> Json.toJson(page.items)))
        case None => BadRequest("")
      }
  }

  def editCollection(id: UUID) = HasTokenAsync() {
    _ => _ => request =>

      logger debug s"Find by id = $id"

      collectionService.findOneBy(CollectionFilter(id = Some(id)))(findOneBy).map {
        case Some(collection) => Ok(Json.obj("collection" -> Json.toJson(collection)))
        case None => NotFound(Json.obj("msg" -> "collection.not.found"))
      }
  }

  def updateCollection(id: UUID) = HasTokenAsync(parse.json) {
    _ => _ => request =>

      logger debug s"Update a Collection = $request"

      request.body.validate[Collection].map {
        collection => collectionService.update(id, collection)(update)(findBy)(completeUpdate).map {
          case Left(error) => BadRequest(Json.obj("msg" -> error.message))
          case Right(success) => Ok(Json.obj("msg" -> success.message))
        }
      }.getOrElse(Future.successful(BadRequest("invalid json")))
  }

  def removeCollection(id: UUID) = HasTokenAsync() {
    _ => _ => request =>

      logger debug s"Removing id = $id"

      collectionService.remove(id)(remove)(findOneBy).map {
        case Left(error) => BadRequest(Json.obj("msg" -> error.message))
        case Right(success) => Ok(Json.obj("msg" -> success.message))
      }
  }

  def completeCollection(collection: String) = HasTokenAsync() {
    _ => _ => request =>

      logger debug s"Collection $collection is complete."
      //
      //      collectionService.remove(id).map {
      //        case Left(error) => BadRequest(Json.obj("msg" -> error.message))
      //        case Right(success) => Ok(Json.obj("msg" -> success.message))
      //      }
      ???
  }

}
