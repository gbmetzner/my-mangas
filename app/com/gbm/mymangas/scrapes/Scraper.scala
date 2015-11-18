package com.gbm.mymangas.scrapes

import org.jsoup.nodes.Document

/**
  * Created by gbmetzner on 11/15/15.
  */
trait Scraper {
  val baseURL: String
  type R

  def scrape(document: Document): R
}
