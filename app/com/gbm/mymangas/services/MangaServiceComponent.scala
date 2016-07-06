package com.gbm.mymangas.services

import java.io.File
import java.util.UUID

import com.gbm.mymangas.models.Manga
import com.gbm.mymangas.models.filters.Predicate
import com.gbm.mymangas.utils.messages.{ Failed, Succeed }
import reactivemongo.api.commands.WriteResult

import scala.concurrent.Future

/**
 * @author Gustavo Metzner on 10/13/15.
 */
trait MangaServiceComponent {

  def mangaService: MangaService

  trait MangaService extends Service[Manga] {

    def insert(manga: Manga)(f: Manga => Future[WriteResult])(g: Predicate => Future[Option[Manga]]): Future[Either[Failed, Succeed]]

    def update(id: UUID, manga: Manga)(f: (UUID, Manga) => Future[WriteResult])(g: Predicate => Future[Option[Manga]]): Future[Either[Failed, Succeed]]

    def updateOwnership(id: UUID, doIHaveIt: Boolean)(f: (UUID, Manga) => Future[WriteResult])(g: Predicate => Future[Option[Manga]]): Future[Either[Failed, Succeed]]

    def completeUpdate(collectionsName: String, doIHaveIt: Boolean)(f: (UUID, Manga) => Future[WriteResult])(g: Predicate => Future[List[Manga]]): Future[Unit]

    def uploadCover(mangaID: UUID, directory: String, file: File)(f: (UUID, Manga) => Future[WriteResult])(g: Predicate => Future[Option[Manga]]): Future[Either[Failed, Succeed]]

    def latestNumber(collectionName: String)(f: Predicate => Future[Option[Manga]]): Future[Option[Manga]]
  }

}
