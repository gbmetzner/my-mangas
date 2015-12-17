package com.gbm.mymangas.controllers

import java.util.UUID
import javax.inject.Inject

import com.gbm.mymangas.models.Collection
import com.gbm.mymangas.models.filters.CollectionFilter
import com.gbm.mymangas.registries.{CollectionComponentRegistry, MangaComponentRegistry}
import com.gbm.mymangas.repositories.{CollectionRepositoryComponent, MangaRepositoryComponent}
import com.gbm.mymangas.services.{CollectionServiceComponent, MangaServiceComponent}
import com.gbm.mymangas.utils.json.CollectionParser.collectionFormatter
import play.api.i18n.MessagesApi
import play.api.libs.json.Json
import play.api.mvc.Action

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * @author Gustavo Metzner on 10/13/15.
  */
class CollectionController @Inject()(val messagesApi: MessagesApi)
  extends BaseController with CollectionComponentRegistry with MangaComponentRegistry {
  requires: CollectionServiceComponent with CollectionRepositoryComponent
    with MangaServiceComponent with MangaRepositoryComponent =>

  def createCollection = HasTokenAsync(parse.json) {
    _ => _ => request =>

      logger info s"Create a Collection = $request"

      request.body.validate[Collection].map {
        coll => collectionService.insert(coll)(collectionRepository.insert)(collectionRepository.findBy).map {
          case Left(error) => BadRequest(Json.obj("msg" -> withMessage(error.message)))
          case Right(success) => Created(Json.obj("msg" -> withMessage(success.message)))
        }
      }.getOrElse(Future.successful(BadRequest(withMessage("error.invalid.json"))))
  }

  def searchCollections = Action.async {
    request =>

      val publisher = request.getQueryString("publisher")
      val name = request.getQueryString("name")
      val limit = request.getQueryString("limit").map(_.toInt)
      val skip = request.getQueryString("skip").map(_.toInt)

      logger debug s"Search by publisher = $publisher, name = $name, limit = $limit and skip = $skip for request = $request"

      collectionService.search(CollectionFilter(name = name, limit = limit, skip = skip))(collectionRepository.search).map {
        case Some(page) => Ok(Json.obj("totalRecords" -> page.totalRecords, "items" -> Json.toJson(page.items)))
        case None => BadRequest("")
      }
  }

  def editCollection(id: UUID) = HasTokenAsync() {
    _ => _ => request =>

      logger debug s"Find by id = $id"

      collectionService.findOneBy(CollectionFilter(id = Some(id)))(collectionRepository.findOneBy).map {
        case Some(collection) => Ok(Json.obj("collection" -> Json.toJson(collection)))
        case None => NotFound(Json.obj("msg" -> withMessage("collection.not.found")))
      }
  }

  def updateCollection(id: UUID) = HasTokenAsync(parse.json) {
    _ => _ => request =>

      logger debug s"Update a Collection = $request"

      request.body.validate[Collection].map {
        collection => collectionService.update(id, collection)(collectionRepository.update)(collectionRepository.findBy) {
          (collection, doIHaveIt) =>
            mangaService.completeUpdate(collection, doIHaveIt)(mangaRepository.update)(mangaRepository.findBy)
        }.map {
          case Left(error) => BadRequest(Json.obj("msg" -> withMessage(error.message)))
          case Right(success) => Ok(Json.obj("msg" -> withMessage(success.message)))
        }
      }.getOrElse(Future.successful(BadRequest(withMessage("error.invalid.json"))))
  }

  def removeCollection(id: UUID) = HasTokenAsync() {
    _ => _ => request =>

      logger debug s"Removing id = $id"

      collectionService.remove(id)(collectionRepository.remove)(collectionRepository.findOneBy).map {
        case Left(error) => BadRequest(Json.obj("msg" -> withMessage(error.message)))
        case Right(success) => Ok(Json.obj("msg" -> withMessage(success.message)))
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
