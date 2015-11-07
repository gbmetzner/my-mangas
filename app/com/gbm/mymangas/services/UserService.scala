package com.gbm.mymangas.services

import javax.inject.Inject

import com.gbm.mymangas.models.User
import com.gbm.mymangas.models.filters.Predicate
import com.gbm.mymangas.utils.json.UserParser.userFormatterService
import com.typesafe.scalalogging.LazyLogging
import play.modules.reactivemongo.ReactiveMongoApi
import play.modules.reactivemongo.json._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * Created by gbmetzner on 11/5/15.
  */
class UserService @Inject()(override val reactiveMongoApi: ReactiveMongoApi)
  extends Service with LazyLogging {

  override protected[services] val collectionName: String = "users"

  def findOneBy(predicate: Predicate): Future[Option[User]] = {

    logger debug s"Finding by $predicate"

    collection.find(predicate.filter).one[User]
  }

}
