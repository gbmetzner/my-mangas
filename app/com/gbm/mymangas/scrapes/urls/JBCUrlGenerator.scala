package com.gbm.mymangas.scrapes.urls

import com.gbm.mymangas.utils.browser.Browser
import org.jsoup.nodes.Document

/**
  * Created by gbmetzner on 11/18/15.
  */
case class JBCUrlGenerator(override val browser: Browser) extends UrlGenerator {
  override val baseURL: String = "http://mangasjbc.uol.com.br/"

  override def generate(searchParam: String, latestNumber: Int): Seq[Document] = {
    generateURLs(searchParam, latestNumber, 0)(jbc.docFromPage)(jbc.verifyLatestEdition)
  }

}
