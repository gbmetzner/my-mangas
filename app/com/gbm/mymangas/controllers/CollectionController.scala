package com.gbm.mymangas.controllers

import java.util.UUID
import javax.inject.Inject

import com.gbm.mymangas.models.Collection
import com.gbm.mymangas.models.filters.CollectionFilter
import com.gbm.mymangas.registries.{CollectionComponent, MangaComponent}
import com.gbm.mymangas.repositories.{CollectionRepository, MangaRepository}
import com.gbm.mymangas.services.CollectionService
import com.gbm.mymangas.services.impl.MangaService
import com.gbm.mymangas.utils.json.CollectionParser.collectionFormatter
import play.api.cache.CacheApi
import play.api.i18n.MessagesApi
import play.api.libs.json.Json
import play.api.mvc.Action

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * @author Gustavo Metzner on 10/13/15.
  */
class CollectionController @Inject()(val messagesApi: MessagesApi,
                                     val cacheApi: CacheApi,
                                     val collectionComponent: CollectionComponent,
                                     val mangaComponent: MangaComponent)
  extends BaseController {

  private val collectionService: CollectionService = collectionComponent.collectionService
  private val collectionRepository: CollectionRepository = collectionComponent.collectionRepository
  private val mangaService: MangaService = mangaComponent.mangaService
  private val mangaRepository: MangaRepository = mangaComponent.mangaRepository

  def createCollection = hasTokenAsync(parse.json) { _ => _ => request =>

    logger info s"Create a Collection = $request"

    request.body.validate[Collection].map {
      coll =>
        collectionService.insert(coll)(collectionRepository.insert)(collectionRepository.findOneBy).map {
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

  def editCollection(id: UUID) = hasTokenAsync() { _ => _ => request =>

    logger debug s"Find by id = $id"

    collectionService.findOneBy(CollectionFilter(id = Some(id)))(collectionRepository.findOneBy).map {
      case Some(collection) => Ok(Json.obj("collection" -> Json.toJson(collection)))
      case None => NotFound(Json.obj("msg" -> withMessage("collection.not.found")))
    }
  }

  def updateCollection(id: UUID) = hasTokenAsync(parse.json) { _ => _ => request =>

    logger debug s"Update a Collection = $request"

    request.body.validate[Collection].map {
      collection =>
        collectionService.update(id, collection)(collectionRepository.update)(collectionRepository.findBy) {
          (collection, doIHaveIt) =>
            mangaService.completeUpdate(collection, doIHaveIt)(mangaRepository.update)(mangaRepository.findBy)
        }.map {
          case Left(error) => BadRequest(Json.obj("msg" -> withMessage(error.message)))
          case Right(success) => Ok(Json.obj("msg" -> withMessage(success.message)))
        }
    }.getOrElse(Future.successful(BadRequest(withMessage("error.invalid.json"))))
  }

  def removeCollection(id: UUID) = hasTokenAsync() { _ => _ => request =>

    logger debug s"Removing id = $id"

    collectionService.remove(id)(collectionRepository.remove)(collectionRepository.findOneBy).map {
      case Left(error) => BadRequest(Json.obj("msg" -> withMessage(error.message)))
      case Right(success) => Ok(Json.obj("msg" -> withMessage(success.message)))
    }
  }

  def completeCollection(collection: String) = hasTokenAsync() { _ => _ => request =>

    logger debug s"Collection $collection is complete."
    //
    //      collectionService.remove(id).map {
    //        case Left(error) => BadRequest(Json.obj("msg" -> error.message))
    //        case Right(success) => Ok(Json.obj("msg" -> success.message))
    //      }
    ???
  }

}
