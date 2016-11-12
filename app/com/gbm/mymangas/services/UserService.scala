package com.gbm.mymangas.services

import java.util.UUID

import com.gbm.mymangas.models.User
import com.gbm.mymangas.models.filters.Predicate
import com.gbm.mymangas.utils.messages.{ Failed, Succeed }
import reactivemongo.api.commands.WriteResult

import scala.concurrent.Future

/**
 * @author Gustavo Metzner on 11/5/15.
 */
class UserService extends Service[User] {
  override def remove(id: UUID)(f: (UUID) => Future[WriteResult])(g: (Predicate) => Future[Option[User]]): Future[Either[Failed, Succeed]] = ???
}
