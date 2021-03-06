package com.gbm.mymangas.scrapes.urls

import com.gbm.mymangas.utils.browser.Browser
import net.ruippeixotog.scalascraper.model.Document

/**
 * Created by gbmetzner on 11/18/15.
 */
case class JBCPagesFinder(override val browser: Browser) extends PagesFinder {
  override val baseURL: String = "http://mangasjbc.com.br/"

  override def generate(searchParam: String, latestNumber: Int): Seq[Document] = {
    generateURLs(searchParam, latestNumber, 0)(JBC.docFromPage)(JBC.verifyLatestEdition)
  }

}
