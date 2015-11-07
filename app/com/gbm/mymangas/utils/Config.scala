package com.gbm.mymangas.utils

import play.api.Play.current

import scala.concurrent.duration.Duration

/**
  * @author Gustavo Metzner on 10/10/15.
  */
object Config {

  private val config = play.api.Play.configuration

  lazy val defaultCover = getStringFromKey("manga.no.cover")

  lazy val smartFileKey: String = getStringFromKey("smartfile.key")

  lazy val smartFileSecret: String = getStringFromKey("smartfile.secret")

  lazy val smartFileApiUrl: String = getStringFromKey("smartfile.api.url")

  lazy val cacheDuration: Duration = getDuration("cache.expiration")

  def getString(key: String) = getStringFromKey(key)

  private def getDuration(key: String): Duration = Duration(getStringFromKey(key))

  private def getStringFromKey(key: String): String = {
    config.getString(key).getOrElse(throw new IllegalArgumentException(s"key $key not found."))
  }
}