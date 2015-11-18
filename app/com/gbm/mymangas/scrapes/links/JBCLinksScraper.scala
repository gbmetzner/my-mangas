package com.gbm.mymangas.scrapes.links

import org.jsoup.nodes.Document

/**
  * Created by gbmetzner on 11/15/15.
  */
object JBCLinksScraper extends LinksScraper {

  override val baseURL: String = "http://mangasjbc.uol.com.br"

  override def scrape(document: Document): Seq[String] = extract(document)(jbc.extractLinks)
}
