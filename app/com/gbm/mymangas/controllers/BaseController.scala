package com.gbm.mymangas.controllers

import com.gbm.mymangas.controllers.security.Security
import com.typesafe.scalalogging.LazyLogging
import play.api.i18n.MessagesApi
import play.api.libs.json.Json
import play.api.mvc.{ Controller, Result }

import scala.concurrent.Future

/**
 * @author Gustavo Metzner on 10/10/15.
 */
trait BaseController
    extends Controller with Security with LazyLogging {

  //  val invalidJson: (Result, String, String) => Result = {
  //    (result, `type`, msg) => result(Json.obj("type" -> `type`, "msg" -> withMessage("error.invalid.json")))
  //  }
  //
  //  val bd = BadGateway
  //
  //  val badRequest:(String, String) => Result = {
  //    (`type`, msg) => bd(Json.obj("type" -> "error", "msg" -> msg))
  //  }

  def messagesApi: MessagesApi

  def withMessage(key: String): String = messagesApi(key)

  def withMessage(key: String, arguments: Any*): String = messagesApi(key, arguments)
}

