package controllers

import java.io.File
import java.util.UUID
import javax.inject.Inject

import models.Manga
import models.filters.MangaFilter
import play.api.libs.json.Json
import play.api.mvc.Action
import services.MangaService
import utils.json.MangaParser.{mangaFormatterController, queryString2Predicate}

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
          case Left(error) => BadRequest(error.message)
          case Right(success) => Created(success.message)
        }
      }.getOrElse(Future.successful(BadRequest("invalid json")))
  }

  def findByName(name: String) = Action.async {

    logger info s"Find by name = $name"

    mangaService.findBy(MangaFilter(name = Option(name))).map {
      mangas => Ok(Json.toJson(mangas))
    }
  }

  def update(id: UUID) = Action.async(parse.json) {
    request =>

      logger info s"Update manga = ($request)"

      request.body.validate[Manga].map {
        manga => mangaService.update(manga.copy(id = id)).map {
          case Left(error) => BadRequest(error.message)
          case Right(success) => Created(success.message)
        }
      }.getOrElse(Future.successful(BadRequest("invalid json")))
  }

  def filter = Action.async {
    request =>

      val predicate = queryString2Predicate(request)
      mangaService.findBy(predicate).map {
        mangas => Ok(Json.toJson(mangas))
      }
  }

  def uploadCover = Action.async(parse.multipartFormData) {
    request =>

      val directory = request.getQueryString("directory").get
      val filename = request.getQueryString("filename").get
      val mangaID = request.getQueryString("mangaID").map(UUID.fromString).get

      request.body.file(request.getQueryString("filename").get).map {
        picture =>

          val file = picture.ref.moveTo(new File(s"/tmp/$filename"))
          mangaService.uploadCover(mangaID, directory, file)
          file.delete()

          Future.successful(Ok("File uploaded"))
      }.getOrElse {
        Future.successful(Redirect(routes.Application.index()).flashing("error" -> "Missing file"))
      }
  }

  def latestNumber(collectionName: String) = Action.async {
    mangaService.latestNumber(collectionName).map {
      case Some(manga) => Ok(Json.obj("data" -> Json.toJson(manga)))
      case None => NotFound(Json.obj("msg" -> "manga.not.found"))
    }
  }
}
