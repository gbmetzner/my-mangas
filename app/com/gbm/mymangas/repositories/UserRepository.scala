package com.gbm.mymangas.repositories

import javax.inject.Inject

import com.gbm.mymangas.models.User
import com.gbm.mymangas.models.filters.Predicate
import com.gbm.mymangas.utils.json.UserParser.userFormatterRepo
import play.modules.reactivemongo.ReactiveMongoApi
import play.modules.reactivemongo.json._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * Created by gbmetzner on 12/6/15.
  */
class UserRepository @Inject()(val reactiveMongoApi: ReactiveMongoApi) extends Repository {
  override protected[repositories] val collectionName: String = "users"

  def findOneBy(predicate: Predicate): Future[Option[User]] = {
    collection.find(predicate.filter).one[User]
  }

}
