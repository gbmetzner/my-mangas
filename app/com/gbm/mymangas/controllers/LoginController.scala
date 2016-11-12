package com.gbm.mymangas.controllers

import javax.inject.Inject

import com.gbm.mymangas.models.Login
import com.gbm.mymangas.models.filters.UserFilter
import com.gbm.mymangas.registries.UserComponent
import com.gbm.mymangas.repositories.UserRepository
import com.gbm.mymangas.services.UserService
import com.gbm.mymangas.utils.Password.EncryptPassword
import com.gbm.mymangas.utils.UUID.generate
import com.gbm.mymangas.utils.json.UserParser.{loginFormatterController, userFormatterController}
import play.api.cache.CacheApi
import play.api.i18n.MessagesApi
import play.api.libs.json.Json
import play.api.mvc.Action

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.language.postfixOps

/**
  * @author Gustavo Metzner on 11/5/15.
  */
class LoginController @Inject()(val messagesApi: MessagesApi,
                                val cacheApi: CacheApi,
                                val userComponent: UserComponent) extends BaseController {

  private val userService: UserService = userComponent.userService
  private val userRepository: UserRepository = userComponent.userRepository

  def login = Action.async(parse.json) {
    request =>

      request.body.validate[Login].map {
        login =>
          val userFilter = UserFilter(Some(login.username), Some(login.password.encryptPassword))
          userService.findOneBy(userFilter)(userRepository.findOneBy).map {
            case Some(user) =>
              val token = generate().toString
              Ok(Json.obj("authToken" -> token, "user" -> Json.toJson(user))).withToken(token -> user)
            case None => NotFound(Json.obj("msg" -> withMessage("login.invalid.credentials")))
          }
      }.getOrElse(Future.successful(BadRequest(withMessage("error.invalid.json"))))
  }

  def logout = Action.async {
    request =>
      Future {
        request.headers.get(AuthTokenHeader).fold {
          logger warn s"Token not found during logout."
          BadRequest(Json.obj("msg" -> withMessage("error.general")))
        } {
          token =>
            logger debug s"Logging out for token = $token"
            Ok(Json.obj("msg" -> withMessage("login.logged.out"))).discardingToken(token)
        }
      }
  }

  def logged = hasTokenAsync() {
    token => user => request =>
      logger debug s"Retrieving user data for $user"
      Future.successful(Ok(Json.obj("user" -> Json.toJson(user))))
  }

}