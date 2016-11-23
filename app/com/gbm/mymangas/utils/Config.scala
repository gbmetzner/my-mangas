package com.gbm.mymangas.utils

import com.typesafe.config.ConfigFactory

import scala.concurrent.duration.Duration

/**
  * @author Gustavo Metzner on 10/10/15.
  */
object Config {

  private val config = ConfigFactory.load("application.conf")

  lazy val defaultCover: String = getStringFromKey("manga.no.cover")

  lazy val smartFileKey: String = getStringFromKey("smartfile.key")

  lazy val smartFileSecret: String = getStringFromKey("smartfile.secret")

  lazy val smartFileApiUrl: String = getStringFromKey("smartfile.api.url")

  lazy val cacheDuration: Duration = getDuration("cache.expiration")

  lazy val hostname: String = getStringFromKey("hostname")

  lazy val smartFilePath: String = getStringFromKey("smartfile.base.path")

  def getString(key: String): String = getStringFromKey(key)

  private def getDuration(key: String): Duration = Duration(getStringFromKey(key))

  private def getStringFromKey(key: String): String = {
    Option(config.getString(key)).getOrElse(throw new IllegalArgumentException(s"key $key not found."))
  }
}
