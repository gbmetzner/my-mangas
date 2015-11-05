package com.gbm.mymangas.controllers

import java.io.File
import java.util.UUID
import javax.inject.Inject
import com.gbm.mymangas.utils.StandardizeNames._
import com.gbm.mymangas.models.Manga
import com.gbm.mymangas.models.filters.MangaFilter
import com.gbm.mymangas.services.MangaService
import com.gbm.mymangas.utils.json.MangaParser.{mangaFormatterController, queryString2Predicate}
import play.api.libs.json.Json
import play.api.mvc.Action

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
 * @author Gustavo Metzner on 10/13/15.
 */
class MangaController @Inject()(mangaService: MangaService) extends BaseController {

  def create = Action.async(parse.json) {
    request =>

      logger info s"Create a Manga = $request"

      request.body.validate[Manga].map {
        manga => mangaService.insert(manga).map {
          case Left(error) => BadRequest(Json.obj("msg" -> error.message))
          case Right(success) => Created(Json.obj("msg" -> success.message))
        }
      }.getOrElse(Future.successful(BadRequest("invalid json")))
  }

  def update(id: UUID) = Action.async(parse.json) {
    request =>

      logger info s"Update manga = ($request)"

      request.body.validate[Manga].map {
        manga => mangaService.update(manga.copy(id = id)).map {
          case Left(error) => BadRequest(Json.obj("msg" -> error.message))
          case Right(success) => Created(Json.obj("msg" -> success.message))
        }
      }.getOrElse(Future.successful(BadRequest("invalid json")))
  }

  def search = Action.async {
    request =>

      val predicate = queryString2Predicate(request)

      logger debug s"Search by predicate = $predicate"

      mangaService.search(predicate).map {
        case Some(page) => Ok(Json.obj("totalRecords" -> page.totalRecords, "items" -> Json.toJson(page.items)))
        case None => BadRequest("")
      }
  }

  def edit(id: UUID) = Action.async {
    logger debug s"Find by id = $id"

    mangaService.findOneBy(MangaFilter(id = Some(id))).map {
      case Some(manga) => Ok(Json.obj("manga" -> Json.toJson(manga)))
      case None => NotFound(Json.obj("msg" -> "manga.not.found"))
    }
  }

  def uploadCover = Action.async(parse.multipartFormData) {
    request =>

      val directory = request.getQueryString("directory").get
      val filename = request.getQueryString("originalname").get
      val mangaID = request.getQueryString("mangaID").map(UUID.fromString).get

      logger debug s"Request upload = $request"

      request.body.file("file").map {
        picture =>

          val file = picture.ref.moveTo(new File(s"/tmp/$filename.jpg".standardize))
          mangaService.uploadCover(mangaID, directory.standardize, file)
          file.delete()

          Future.successful(Ok(Json.obj("msg" -> "cover.uploaded")))
      }.getOrElse {
        Future.successful(NotFound(Json.obj("msg" -> "cover.not.found")))
      }
  }

  def latestNumber(collectionName: String) = Action.async {
    mangaService.latestNumber(collectionName).map {
      case Some(manga) => Ok(Json.obj("data" -> Json.toJson(manga)))
      case None => NotFound(Json.obj("msg" -> "manga.not.found"))
    }
  }

  def remove(id: UUID) = Action.async {
    logger debug s"Removing id = $id"

    mangaService.remove(id).map {
      case Left(error) => BadRequest(Json.obj("msg" -> error.message))
      case Right(success) => Ok(Json.obj("msg" -> success.message))
    }
  }
}
