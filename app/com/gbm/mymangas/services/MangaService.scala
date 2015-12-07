package com.gbm.mymangas.services

import java.io.File
import java.util.UUID

import com.gbm.mymangas.models.filters.{MangaFilter, Predicate}
import com.gbm.mymangas.models.{Manga, Page}
import com.gbm.mymangas.utils.messages.{Error, Failed, Succeed, Warning}
import com.gbm.mymangas.utils.{Config, FileUpload}
import org.joda.time.DateTime
import play.api.libs.json.{JsObject, Json}
import reactivemongo.api.commands.WriteResult

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * @author Gustavo Metzner on 10/13/15.
  */
class MangaService {

  def insert(manga: Manga)(f: Manga => Future[WriteResult])(g: Predicate => Future[List[Manga]]): Future[Either[Failed, Succeed]] = {
    findBy(MangaFilter(name = Option(manga.name), number = Option(manga.number)))(g).flatMap {
      mangas => if (mangas.isEmpty) {
        f(manga).map {
          lastError => if (lastError.hasErrors) Left(Error(lastError.message)) else Right(Succeed("manga.added"))
        }
      }
      else Future.successful(Left(Error("manga.already.exists")))
    }
  }

  def findBy(predicate: Predicate)(f: Predicate => Future[List[Manga]]): Future[List[Manga]] = {
    f(predicate)
  }

  def findOneBy(predicate: Predicate)(f: Predicate => Future[Option[Manga]]): Future[Option[Manga]] = {
    f(predicate)
  }

  def search(predicate: Predicate)(f: Predicate => Future[Option[Page[Manga]]]): Future[Option[Page[Manga]]] = {
    f(predicate)
  }

  def update(id: UUID, manga: Manga)(f: (UUID, Manga) => Future[WriteResult])(g: Predicate => Future[Option[Manga]]): Future[Either[Failed, Succeed]] = {
    findOneBy(MangaFilter(id = Option(manga.id)))(g).flatMap {
      case Some(m) =>

        val link = manga.publicLink match {
          case Config.defaultCover => m.publicLink
          case _ => manga.publicLink
        }

        f(id, manga.copy(publicLink = link, createdAt = m.createdAt, updatedAt = DateTime.now())).map {
          lastError => if (lastError.hasErrors) Left(Error(lastError.message)) else Right(Succeed("manga.updated"))
        }
      case None => Future.successful(Left(Warning("manga.not.found")))
    }
  }

  def updateOwnership(id: UUID, doIHaveIt: Boolean)(f: (UUID, Manga) => Future[WriteResult])(g: Predicate => Future[Option[Manga]]): Future[Either[Failed, Succeed]] = {
    findOneBy(MangaFilter(id = Option(id)))(g).flatMap {
      case Some(m) =>
        f(id, m.copy(doIHaveIt = doIHaveIt, updatedAt = DateTime.now())).map {
          lastError => if (lastError.hasErrors) Left(Error(lastError.message)) else Right(Succeed("manga.updated"))
        }
      case None => Future.successful(Left(Warning("manga.not.found")))
    }
  }

  def completeUpdate(collectionsName: String, doIHaveIt: Boolean)(f: (UUID, Manga) => Future[WriteResult])(g: Predicate => Future[List[Manga]]): Unit = {
    //logger debug s"Updating mangas for collection = $collectionsName with $doIHaveIt"

    findBy(MangaFilter(collection = Some(collectionsName)))(g).foreach {
      _.foreach(manga => f(manga.id, manga.copy(doIHaveIt = doIHaveIt)))
    }
  }

  def remove(id: UUID)(f: UUID => Future[WriteResult])(g: Predicate => Future[Option[Manga]]): Future[Either[Failed, Succeed]] = {
    findOneBy(MangaFilter(id = Option(id)))(g).flatMap {
      case Some(_) => f(id).map {
        lastError =>
          if (lastError.hasErrors) Left(Error(lastError.message))
          else Right(Succeed("manga.removed"))
      }
      case None => Future.successful(Left(Error("manga.not.found")))
    }
  }

  def uploadCover(mangaID: UUID, directory: String, file: File)(f: (UUID, Manga) => Future[WriteResult])(g: Predicate => Future[Option[Manga]]): Future[Either[Failed, Succeed]] = {

    val publicLink = FileUpload.upload(s"/my-mangas/mangas/$directory", file)

    findOneBy(MangaFilter(id = Some(mangaID)))(g).flatMap {
      case Some(manga) =>
        val updatedManga = manga.copy(publicLink = publicLink, updatedAt = DateTime.now())
        f(mangaID, updatedManga).map {
          lastError => if (lastError.hasErrors) Left(Error(lastError.message)) else Right(Succeed("manga.updated"))
        }
      case None => Future.successful(Left(Warning("manga.not.found")))
    }
  }

  def latestNumber(collectionName: String)(f: Predicate => Future[Option[Manga]]): Future[Option[Manga]] = {

    val predicate = new MangaFilter(collection = Some(collectionName)) {
      override def sort: JsObject = {
        Json.obj("number" -> -1)
      }
    }
    f(predicate)
  }
}
