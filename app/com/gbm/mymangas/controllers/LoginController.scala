package com.gbm.mymangas.controllers

import javax.inject.Inject

import com.gbm.mymangas.models.{User, Login}
import com.gbm.mymangas.models.filters.UserFilter
import com.gbm.mymangas.services.UserService
import com.gbm.mymangas.utils.Password.EncryptPassword
import com.gbm.mymangas.utils.UUID.generate
import com.gbm.mymangas.utils.json.UserParser.{loginFormatterController, userFormatterController}
import play.api.cache.CacheApi
import play.api.i18n.MessagesApi
import play.api.libs.json.Json
import play.api.mvc.Action
import play.cache.NamedCache

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.language.postfixOps

/**
  * Created by gbmetzner on 11/5/15.
  */
class LoginController @Inject()(userService: UserService,
                                val messagesApi: MessagesApi) extends BaseController {

  def login = Action.async(parse.json) {
    request =>

      request.body.validate[Login].map {
        login => userService.findOneBy(UserFilter(username = Some(login.username), password = Some(login.password.encryptPassword))).map {
          case Some(user) =>
            val token = generate().toString
            Ok(Json.obj("authToken" -> token, "user" -> Json.toJson(user))).withToken(token -> user)
          case None => NotFound(Json.obj("msg" -> "invalid.credentials"))
        }
      }.getOrElse(Future.successful(BadRequest("invalid json")))
  }

  def logout = Action.async {
    request => Future {
      request.headers.get(AuthTokenHeader) map {
        token => Ok(Json.obj("msg"->"user.logged.out")).discardingToken(token)
      } getOrElse BadRequest(Json.obj("msg" -> "No Token"))
    }
  }

  def logged = HasTokenAsync() {
    token => user => request =>
      Future.successful(Ok(Json.obj("user" -> Json.toJson(user))))
  }

}