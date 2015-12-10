package com.gbm.mymangas.services.impl

import java.util.UUID

import com.gbm.mymangas.models.User
import com.gbm.mymangas.models.filters.Predicate
import com.gbm.mymangas.services.UserServiceComponent
import com.gbm.mymangas.utils.messages.{Succeed, Failed}
import reactivemongo.api.commands.WriteResult

import scala.concurrent.Future

/**
  * Created by gbmetzner on 12/8/15.
  */
trait UserServiceComponentImpl extends UserServiceComponent {

  override def userService: UserService = new UserServiceImpl

  class UserServiceImpl extends UserService {
    override def remove(id: UUID)(f: (UUID) => Future[WriteResult])(g: (Predicate) => Future[Option[User]]): Future[Either[Failed, Succeed]] = ???
  }

}
