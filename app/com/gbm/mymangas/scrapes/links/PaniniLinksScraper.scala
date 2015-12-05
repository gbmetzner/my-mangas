package com.gbm.mymangas.scrapes.links

import com.gbm.mymangas.utils.browser.Browser
import org.jsoup.nodes.Document

/**
  * Created by gbmetzner on 11/15/15.
  */
case class PaniniLinksScraper(override val browser: Browser) extends LinksScraper {

  override val baseURL: String = "http://www.paninicomics.com.br"

  override def scrape(document: Document): Seq[Document] = extract(document)(panini.extractLinks)

}
