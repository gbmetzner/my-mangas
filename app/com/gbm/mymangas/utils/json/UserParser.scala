package com.gbm.mymangas.utils.json

import com.gbm.mymangas.models.{Login, User}
import play.api.libs.json.Reads._
import play.api.libs.json._

/**
  * Created by gbmetzner on 11/6/15.
  */
object UserParser {

  private val reads: Reads[User] = new Reads[User] {
    override def reads(json: JsValue): JsResult[User] = {
      val name = (json \ "name").as[String](minLength[String](5))
      val username = (json \ "username").as[String](minLength[String](5))
      JsSuccess(User(name = name, username = username, password = ""))
    }
  }

  private val writes: Writes[User] = new Writes[User] {
    override def writes(user: User): JsValue = {
      Json.obj(
        "id" -> user.id,
        "name" -> user.name,
        "username" -> user.username,
        "createdAt" -> user.createdAt,
        "updatedAt" -> user.updatedAt
      )
    }
  }

  implicit val userFormatterController = Format(reads, writes)

  implicit val userFormatterRepo = Json.format[User]

  implicit val loginFormatterController = Json.format[Login]

}
