package com.gbm.mymangas.utils

import play.api.Play.current

/**
 * @author Gustavo Metzner on 10/10/15.
 */
object Config {

  private val config = play.api.Play.configuration

  lazy val defaultMangaImage = getStringFromKey("manga.no.cover")

  lazy val smartFileKey: String = getStringFromKey("smartfile.key")

  lazy val smartFileSecret: String = getStringFromKey("smartfile.secret")

  lazy val smartFileApiUrl: String = getStringFromKey("smartfile.api.url")

  private def getStringFromKey(key: String): String = {
    config.getString(key).getOrElse(throw new IllegalArgumentException(s"key $key not found."))
  }
}