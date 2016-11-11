package com.gbm.mymangas.services

import java.util.UUID

import com.gbm.mymangas.models.Publisher
import com.gbm.mymangas.models.filters.Predicate
import com.gbm.mymangas.services.impl.PublisherServiceImpl
import com.gbm.mymangas.utils.messages.{Failed, Succeed}
import com.google.inject.ImplementedBy
import reactivemongo.api.commands.WriteResult

import scala.concurrent.Future

/**
  * @author Gustavo Metzner on 10/10/15.
  */

@ImplementedBy(classOf[PublisherServiceImpl])
trait PublisherService extends Service[Publisher] {

  def insert(publisher: Publisher)(f: Publisher => Future[WriteResult])(g: Predicate => Future[Option[Publisher]]): Future[Either[Failed, Succeed]]

  def update(id: UUID, publisher: Publisher)(f: (UUID, Publisher) => Future[WriteResult])
            (g: Predicate => Future[List[Publisher]]): Future[Either[Failed, Succeed]]

}
