package com.gbm.mymangas.scrapes.mangas

import com.gbm.mymangas.models.Manga
import org.jsoup.nodes.Document

/**
  * Created by gbmetzner on 11/15/15.
  */
object JBCMangaScraper extends MangaScraper {

  override val baseURL: String = "http://mangasjbc.uol.com.br"

  override def scrape(document: Document): (Manga, String) = {
    extract(document)(jbc.extractManga)(jbc.extractCoverLink)
  }

}
