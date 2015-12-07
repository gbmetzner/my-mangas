package com.gbm.mymangas.services

import com.gbm.mymangas.models.User
import com.gbm.mymangas.models.filters.Predicate
import com.gbm.mymangas.utils.json.UserParser.userFormatterRepo
import play.modules.reactivemongo.json._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * Created by gbmetzner on 11/5/15.
  */
class UserService {

  def findOneBy(predicate: Predicate)(f: Predicate => Future[Option[User]]): Future[Option[User]] = {
    //logger debug s"Finding by $predicate"
    f(predicate)
  }

}
