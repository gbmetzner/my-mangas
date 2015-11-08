package com.gbm.mymangas.models

import com.gbm.mymangas.utils.Config
import play.api.libs.mailer.{Email => PEmail}

/**
  * Created by gbmetzner on 11/7/15.
  */
object Email {
  def apply(to: String, from: String, qtyMangas: Int): PEmail = {
    val subject = "My Mangas - New Publications for you!"
    val bodyHtml = Some(
      s"""<html>
          |<body>
          |<p>There are <b>$qtyMangas</b> new mangas for you!</p>
          |<p>Check these out <a href="${Config.hostname}">here</a>.</p>
          |</body>
          |</html>""".stripMargin)
    PEmail(subject, from, Seq(to), bodyHtml = bodyHtml)
  }
}
