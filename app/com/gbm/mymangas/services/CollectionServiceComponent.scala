package com.gbm.mymangas.services

import java.util.UUID

import com.gbm.mymangas.models.Collection
import com.gbm.mymangas.models.filters.Predicate
import com.gbm.mymangas.utils.messages.{Failed, Succeed}
import reactivemongo.api.commands.WriteResult

import scala.concurrent.Future

/**
  * @author Gustavo Metzner on 10/13/15.
  */
trait CollectionServiceComponent {

  def collectionService: CollectionService

  trait CollectionService extends Service[Collection] {

    def insert(coll: Collection)(f: Collection => Future[WriteResult])(g: Predicate => Future[Option[Collection]]): Future[Either[Failed, Succeed]]

    def update(id: UUID, coll: Collection)(f: (UUID, Collection) => Future[WriteResult])(g: Predicate => Future[List[Collection]])(h: (String, Boolean) => Future[Unit]): Future[Either[Failed, Succeed]]
  }

}
