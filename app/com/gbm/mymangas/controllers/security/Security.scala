package com.gbm.mymangas.controllers.security

import com.gbm.mymangas.models.User
import com.gbm.mymangas.utils.Config._
import play.api.cache.CacheApi
import play.api.i18n.MessagesApi
import play.api.libs.json.Json
import play.api.mvc._

import scala.concurrent.Future

/**
 * Created by gbmetzner on 11/6/15.
 */
trait Security {
  requires: Controller =>

  val messagesApi: MessagesApi
  val cacheApi: CacheApi

  type Id = User

  val AuthTokenHeader = "X-XSRF-TOKEN"
  val AuthTokenCookieKey = "XSRF-TOKEN"
  val AuthTokenUrlKey = "auth"

  /** Checks that a token is either in the header or in the query string */
  def hasTokenAsync[A](p: BodyParser[A] = parse.anyContent)(f: String => Id => Request[A] => Future[Result]): Action[A] =
    Action.async(p) {
      request =>
        val maybeToken = request.headers.get(AuthTokenHeader).orElse(request.getQueryString(AuthTokenUrlKey))
        maybeToken flatMap {
          token =>
            cacheApi.get[Id](token) map {
              id =>
                cacheApi.set(token, id, cacheDuration)
                f(token)(id)(request)
            }
        } getOrElse Future.successful(Unauthorized(Json.obj("msg" -> messagesApi("user.not.logged"))))
    }

  def fromCache(token: String): Option[Id] = cacheApi.get[Id](token) match {
    case Some(id) =>
      cacheApi.set(token, id, cacheDuration)
      Some(id)
    case None => None
  }

  implicit class ResultWithToken(result: Result) {

    def withToken(token: (String, Id)): Result = {
      cacheApi.set(token._1, token._2, cacheDuration)
      result.withCookies(Cookie(AuthTokenCookieKey, token._1, None, httpOnly = false))
    }

    def discardingToken(token: String): Result = {
      cacheApi remove token
      result.discardingCookies(DiscardingCookie(name = AuthTokenCookieKey))
    }

  }

}
