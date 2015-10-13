package utils

import play.api.Play.current

/**
 * @author Gustavo Metzner on 10/10/15.
 */
object Config {

  private val config = play.api.Play.configuration

  lazy val facebookClientId = config.getString("").getOrElse(throwException(""))

  private def throwException(message: String) = {
    throw new IllegalArgumentException(message)
  }

}