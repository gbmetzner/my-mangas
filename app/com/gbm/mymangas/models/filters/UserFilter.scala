package com.gbm.mymangas.models.filters

import play.api.libs.json.{ JsArray, Json, JsObject }

/**
 * Created by gbmetzner on 11/5/15.
 */
case class UserFilter(
  username: Option[String] = None,
    password: Option[String] = None,
    override val limit: Option[Int] = None,
    override val skip: Option[Int] = None
) extends Predicate {

  override def filter: JsObject = {

    val filter = (username.map(u => Json.obj("username" -> u))
      :: password.map(p => Json.obj("password" -> p))
      :: Nil).map(_.getOrElse(JsObject(Nil)))

    Json.obj("$and" -> JsArray(filter))

  }

  override def sort: JsObject = Json.obj("createdAt" -> -1)
}
