package com.gbm.mymangas.controllers

import java.io.File
import java.util.UUID
import javax.inject.Inject

import com.gbm.mymangas.models.Manga
import com.gbm.mymangas.models.filters.MangaFilter
import com.gbm.mymangas.registries.MangaComponent
import com.gbm.mymangas.repositories.MangaRepository
import com.gbm.mymangas.services.impl.MangaService
import com.gbm.mymangas.utils.StandardizeNames._
import com.gbm.mymangas.utils.files.upload.FileUploaderComponentImpl
import com.gbm.mymangas.utils.json.MangaParser.{mangaFormatterController, queryString2Predicate}
import play.api.cache.CacheApi
import play.api.i18n.MessagesApi
import play.api.libs.json.Json
import play.api.mvc.Action

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * @author Gustavo Metzner on 10/13/15.
  */
class MangaController @Inject()(val messagesApi: MessagesApi,
                                val cacheApi: CacheApi,
                                val mangaComponent: MangaComponent) extends BaseController with FileUploaderComponentImpl {

  private val mangaService: MangaService = mangaComponent.mangaService
  private val mangaRepository: MangaRepository = mangaComponent.mangaRepository

  def createManga = hasTokenAsync(parse.json) { _ => _ => request =>

    logger info s"Create a Manga = $request"

    request.body.validate[Manga].map {
      manga =>
        mangaService.insert(manga)(mangaRepository.insert)(mangaRepository.findOneBy).map {
          case Left(error) => BadRequest(Json.obj("msg" -> withMessage(error.message)))
          case Right(success) => Created(Json.obj("msg" -> withMessage(success.message)))
        }
    }.getOrElse(Future.successful(BadRequest(withMessage("error.invalid.json"))))
  }

  def updateManga(id: UUID) = hasTokenAsync(parse.json) { _ => _ => request =>

    logger info s"Update manga = $request"

    request.body.validate[Manga].map {
      manga =>
        mangaService.update(id, manga.copy(id = id))(mangaRepository.update)(mangaRepository.findOneBy).map {
          case Left(error) => BadRequest(Json.obj("msg" -> withMessage(error.message)))
          case Right(success) => Created(Json.obj("msg" -> withMessage(success.message)))
        }
    }.getOrElse(Future.successful(BadRequest(withMessage("error.invalid.json"))))
  }

  def searchMangas = Action.async {
    request =>

      val predicate = queryString2Predicate(request)

      logger debug s"Search by predicate = $predicate"

      mangaService.search(predicate)(mangaRepository.search).map {
        case Some(page) => Ok(Json.obj("totalRecords" -> page.totalRecords, "items" -> Json.toJson(page.items)))
        case None => BadRequest("")
      }
  }

  def editManga(id: UUID) = hasTokenAsync() { _ => _ => request =>

    logger debug s"Find by id = $id"

    mangaService.findOneBy(MangaFilter(id = Some(id)))(mangaRepository.findOneBy).map {
      case Some(manga) => Ok(Json.obj("manga" -> Json.toJson(manga)))
      case None => NotFound(Json.obj("msg" -> withMessage("manga.not.found")))
    }
  }

  def uploadMangaCover = hasTokenAsync(parse.multipartFormData) { _ => _ => request =>

    val directory = request.getQueryString("directory").get
    val filename = request.getQueryString("originalname").get
    val mangaID = request.getQueryString("mangaID").map(UUID.fromString).get

    logger debug s"Request upload = $request"

    request.body.file("file").fold(Future.successful(NotFound(Json.obj("msg" -> withMessage("cover.not.found"))))) {
      picture =>

        val file = picture.ref.moveTo(new File(s"/tmp/$filename.jpg".standardize))
        mangaService.uploadCover(mangaID, directory.standardize, file)(fileUploader.upload)(mangaRepository.update)(mangaRepository.findOneBy)
        file.delete()
        Future.successful(Ok(Json.obj("msg" -> withMessage("cover.uploaded"))))
    }
  }

  def latestMangaNumber(collectionName: String) = hasTokenAsync() { _ => _ => request =>
    mangaService.latestNumber(collectionName)(mangaRepository.findOneBy).map {
      case Some(manga) => Ok(Json.obj("data" -> Json.toJson(manga)))
      case None => NotFound(Json.obj("msg" -> withMessage("manga.not.found")))
    }
  }

  def removeManga(id: UUID) = hasTokenAsync() { _ => _ => request =>

    logger debug s"Removing id = $id"

    mangaService.remove(id)(mangaRepository.remove)(mangaRepository.findOneBy).map {
      case Left(error) => BadRequest(Json.obj("msg" -> withMessage(error.message)))
      case Right(success) => Ok(Json.obj("msg" -> withMessage(success.message)))
    }
  }

  def updateMangaOwnership(id: UUID) = hasTokenAsync(parse.json) { _ => _ => request =>

    logger info s"Update manga ownership = $request"

    request.body.validate[Boolean].map {
      doIHaveIt =>
        mangaService.updateOwnership(id, doIHaveIt)(mangaRepository.update)(mangaRepository.findOneBy).map {
          case Left(error) => BadRequest(Json.obj("msg" -> withMessage(error.message)))
          case Right(success) => Created(Json.obj("msg" -> withMessage(success.message)))
        }
    }.getOrElse(Future.successful(BadRequest(withMessage("error.invalid.json"))))
  }

}
