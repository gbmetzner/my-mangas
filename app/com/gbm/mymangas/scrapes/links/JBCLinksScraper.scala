package com.gbm.mymangas.scrapes.links

import com.gbm.mymangas.utils.browser.Browser
import net.ruippeixotog.scalascraper.model.Document

/**
 * Created by gbmetzner on 11/15/15.
 */
case class JBCLinksScraper(override val browser: Browser) extends LinksScraper {

  override val baseURL: String = ""

  override def scrape(document: Document): Seq[Document] = extract(document)(JBC.extractLinks)
}
