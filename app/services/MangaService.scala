package services

import java.io.File
import java.util.UUID
import javax.inject.Inject

import models.Manga
import models.filters.{MangaFilter, Predicate}
import org.joda.time.DateTime
import play.api.libs.json.Json
import play.modules.reactivemongo.ReactiveMongoApi
import play.modules.reactivemongo.json._
import utils.FileUpload
import utils.json.MangaParser.mangaFormatterService
import utils.messages.{Error, Failed, Succeed, Warning}

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

  def update(manga: Manga)(implicit ec: ExecutionContext): Future[Either[Failed, Succeed]] = {
    findBy(MangaFilter(id = Option(manga.id))).flatMap {
      mangas => if (mangas.nonEmpty) {
        collection.update(Json.obj("id" -> manga.id), manga.copy(createdAt = mangas.head.createdAt, updatedAt = DateTime.now())).map {
          lastError => if (lastError.hasErrors) Left(Error(lastError.message)) else Right(Succeed("manga.updated"))
        }
      }
      else Future.successful(Left(Warning("manga.not.found")))
    }
  }

  def remove(manga: Manga)(implicit ec: ExecutionContext): Future[Either[Failed, Succeed]] = {
    findBy(MangaFilter(id = Option(manga.id))).flatMap {
      mangas => if (mangas.nonEmpty) {
        collection.update(Json.obj("id" -> manga.id), manga.copy(createdAt = mangas.head.createdAt, updatedAt = DateTime.now())).map {
          lastError => if (lastError.hasErrors) Left(Error(lastError.message)) else Right(Succeed("manga.removed"))
        }
      }
      else Future.successful(Left(Warning("manga.not.found")))
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
}
