package com.gbm.mymangas.services.impl

import java.io.File
import java.util.UUID

import com.gbm.mymangas.models.Manga
import com.gbm.mymangas.models.filters.{MangaFilter, Predicate}
import com.gbm.mymangas.services.Service
import com.gbm.mymangas.utils.Config
import com.gbm.mymangas.utils.messages.{Error, Failed, Succeed, Warning}
import org.joda.time.DateTime
import play.api.libs.json.{JsObject, Json}
import reactivemongo.api.commands.WriteResult

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * Defines a Manga Service.
  *
  * @author Gustavo Metzner on 12/8/15.
  */
class MangaService extends Service[Manga] {

  def insert(manga: Manga)(f: Manga => Future[WriteResult])(g: Predicate => Future[Option[Manga]]): Future[Either[Failed, Succeed]] = {
    findOneBy(MangaFilter(collection = Option(manga.collection), number = Option(manga.number)))(g).flatMap {
      mangas =>
        if (mangas.isEmpty) {
          f(manga).map {
            lastError =>
              if (lastError.writeErrors.nonEmpty) {
                logger error s"Error while persisting manga = $manga -> errors = ${lastError.writeErrors.mkString}"
                Left(Error("error.general"))
              } else Right(Succeed("manga.added"))
          }
        } else Future.successful(Left(Error("manga.already.exists")))
    }
  }

  def update(id: UUID, manga: Manga)(f: (UUID, Manga) => Future[WriteResult])(g: Predicate => Future[Option[Manga]]): Future[Either[Failed, Succeed]] = {
    findOneBy(MangaFilter(id = Option(manga.id)))(g).flatMap {
      case Some(m) =>

        val link = manga.publicLink match {
          case Config.defaultCover => m.publicLink
          case _ => manga.publicLink
        }

        f(id, manga.copy(publicLink = link, createdAt = m.createdAt, updatedAt = DateTime.now())).map {
          lastError =>
            if (lastError.writeErrors.nonEmpty) {
              logger error s"Error while updating manga = $manga with errors = ${lastError.writeErrors.mkString}"
              Left(Error("error.general"))
            } else Right(Succeed("manga.updated"))
        }
      case None => Future.successful(Left(Warning("manga.not.found")))
    }
  }

  def updateOwnership(id: UUID, doIHaveIt: Boolean)(f: (UUID, Manga) => Future[WriteResult])(g: Predicate => Future[Option[Manga]]): Future[Either[Failed, Succeed]] = {
    findOneBy(MangaFilter(id = Option(id)))(g).flatMap {
      case Some(m) =>
        f(id, m.copy(doIHaveIt = doIHaveIt, updatedAt = DateTime.now())).map {
          lastError =>
            if (lastError.writeErrors.nonEmpty) {
              logger error s"Error while updating ownership for manga id = $id -> errors = ${lastError.writeErrors.mkString}"
              Left(Error("error.general"))
            } else Right(Succeed("manga.updated"))
        }
      case None => Future.successful(Left(Warning("manga.not.found")))
    }
  }

  def completeUpdate(collectionsName: String, doIHaveIt: Boolean)(f: (UUID, Manga) => Future[WriteResult])(g: Predicate => Future[List[Manga]]): Future[Unit] = {
    logger debug s"Updating mangas for collection = $collectionsName with $doIHaveIt"

    findBy(MangaFilter(collection = Some(collectionsName)))(g).map {
      _.foreach(manga => f(manga.id, manga.copy(doIHaveIt = doIHaveIt, updatedAt = DateTime.now())))
    }
  }

  override def remove(id: UUID)(f: (UUID) => Future[WriteResult])(g: (Predicate) => Future[Option[Manga]]): Future[Either[Failed, Succeed]] = {
    remove(id, "manga.removed", "manga.not.found")(f)(g)
  }

  def uploadCover(mangaID: UUID, directory: String, file: File)(u: (String, File) => String)(f: (UUID, Manga) => Future[WriteResult])(g: Predicate => Future[Option[Manga]]): Future[Either[Failed, Succeed]] = {

    val publicLink = u(s"${Config.smartFilePath}$directory", file)

    findOneBy(MangaFilter(id = Some(mangaID)))(g).flatMap {
      case Some(manga) =>
        val updatedManga = manga.copy(publicLink = publicLink, updatedAt = DateTime.now())
        f(mangaID, updatedManga).map {
          lastError =>
            if (lastError.writeErrors.nonEmpty) {
              logger error s"Error while uploading Cover for manga = $manga with errors = ${lastError.writeErrors.mkString}"
              Left(Error("error.general"))
            } else Right(Succeed("manga.updated"))
        }
      case None => Future.successful(Left(Warning("manga.not.found")))
    }
  }

  def latestNumber(collectionName: String)(f: Predicate => Future[Option[Manga]]): Future[Option[Manga]] = {
    logger debug s"Collection $collectionName latest number"

    val predicate = new MangaFilter(collection = Some(collectionName)) {
      override def sort: JsObject = {
        Json.obj("number" -> -1)
      }
    }
    findOneBy(predicate)(f)
  }
}
