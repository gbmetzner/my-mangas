package com.gbm.mymangas.services

import java.io.File
import java.util.UUID
import javax.inject.Inject

import com.gbm.mymangas.models.filters.{MangaFilter, Predicate}
import com.gbm.mymangas.models.{Manga, Page}
import com.gbm.mymangas.utils.{Config, FileUpload}
import com.gbm.mymangas.utils.json.MangaParser.mangaFormatterService
import com.gbm.mymangas.utils.messages.{Error, Failed, Succeed, Warning}
import org.joda.time.DateTime
import play.api.libs.json.Json
import play.modules.reactivemongo.ReactiveMongoApi
import play.modules.reactivemongo.json._

import scala.concurrent.{ExecutionContext, Future}

/**
  * @author Gustavo Metzner on 10/13/15.
  */
class MangaService @Inject()(val reactiveMongoApi: ReactiveMongoApi) extends Service {

  override protected[services] val collectionName: String = "mangas"

  def insert(manga: Manga)(implicit ec: ExecutionContext): Future[Either[Failed, Succeed]] = {
    findBy(MangaFilter(name = Option(manga.name), number = Option(manga.number))).flatMap {
      mangas => if (mangas.isEmpty) {
        (collection insert manga).map {
          lastError => if (lastError.hasErrors) Left(Error(lastError.message)) else Right(Succeed("manga.added"))
        }
      }
      else Future.successful(Left(Error("manga.already.exists")))
    }
  }

  def findBy(predicate: Predicate)(implicit ec: ExecutionContext): Future[List[Manga]] = {
    collection.find(predicate.filter).
      options(predicate.queryOpts).
      sort(predicate.sort).
      cursor[Manga]().collect[List]()
  }

  def findOneBy(predicate: Predicate)(implicit ec: ExecutionContext): Future[Option[Manga]] = {
    collection.find(predicate.filter).sort(predicate.sort).one[Manga]
  }

  def search(predicate: Predicate)(implicit ec: ExecutionContext): Future[Option[Page[Manga]]] = {
    val totalRecordsFuture = collection.find(predicate.filter).cursor[Manga]().collect[List]().map {
      records => records.size
    }

    val itemsFuture = collection.find(predicate.filter)
      .options(predicate.queryOpts)
      .sort(predicate.sort).cursor[Manga]().collect[List](predicate.queryOpts.batchSizeN)

    for {
      totalRecords <- totalRecordsFuture
      items <- itemsFuture
    } yield Option(Page(totalRecords = totalRecords, items = items))
  }

  def update(manga: Manga)(implicit ec: ExecutionContext): Future[Either[Failed, Succeed]] = {
    findOneBy(MangaFilter(id = Option(manga.id))).flatMap {
      case Some(m) =>

        val link = manga.publicLink match {
          case Config.defaultCover => m.publicLink
          case _ => manga.publicLink
        }

        collection.update(Json.obj("id" -> manga.id), manga.copy(publicLink = link, createdAt = m.createdAt, updatedAt = DateTime.now())).map {
          lastError => if (lastError.hasErrors) Left(Error(lastError.message)) else Right(Succeed("manga.updated"))
        }
      case None => Future.successful(Left(Warning("manga.not.found")))
    }
  }

  def remove(id: UUID)(implicit ec: ExecutionContext): Future[Either[Failed, Succeed]] = {
    findOneBy(MangaFilter(id = Option(id))).flatMap {
      case Some(_) => collection.remove(Json.obj("id" -> id)).map {
        lastError =>
          if (lastError.hasErrors) Left(Error(lastError.message))
          else Right(Succeed("manga.removed"))
      }
      case None => Future.successful(Left(Error("manga.not.found")))
    }
  }

  def uploadCover(mangaID: UUID, directory: String, file: File)(implicit ec: ExecutionContext): Future[Either[Failed, Succeed]] = {

    val publicLink = FileUpload.upload(s"/my-mangas/mangas/$directory", file)

    findBy(MangaFilter(id = Some(mangaID))).flatMap {
      mangas => if (mangas.nonEmpty) {
        val updatedManga = mangas.head.copy(publicLink = publicLink, updatedAt = DateTime.now())

        collection.update(Json.obj("id" -> updatedManga.id), updatedManga).map {
          lastError => if (lastError.hasErrors) Left(Error(lastError.message)) else Right(Succeed("manga.updated"))
        }
      } else Future.successful(Left(Warning("manga.not.found")))
    }
  }

  def latestNumber(collectionName: String)(implicit ec: ExecutionContext): Future[Option[Manga]] = {
    val predicate = MangaFilter(collection = Some(collectionName))
    collection.find(predicate.filter).
      options(predicate.queryOpts).
      sort(Json.obj("number" -> -1)).
      one[Manga]
  }
}
