package com.gbm.mymangas.browser

import com.gbm.mymangas.utils.browser.Browser
import org.jsoup.nodes.Document
import net.ruippeixotog.scalascraper.browser.{Browser => OBrowser}

/**
  * Created by gbmetzner on 11/15/15.
  */
object FakeBrowser extends Browser {

  lazy val browser = new OBrowser

  lazy val urls: Map[String, Document] = Map(
    "berserk_links" -> browser.parseFile(getClass.getResource("/panini/extract_link.html").getPath),
    "berserk_1_manga" -> browser.parseFile(getClass.getResource("/panini/extract_1_manga.html").getPath),
    "http://www.paninicomics.com.br/web/guest/search_product?search=berserk_1_manga" -> browser.parseFile(getClass.getResource("/panini/extract_manga_1.html").getPath),
    "http://www.paninicomics.com.br/web/guest/search_product?search=berserk_2_manga" -> browser.parseFile(getClass.getResource("/panini/extract_manga_2.html").getPath),
    "http://www.paninicomics.com.br/web/guest/search_product?search=berserk_3_manga" -> browser.parseFile(getClass.getResource("/panini/extract_manga_3.html").getPath),
    "video_girl_links" -> browser.parseFile(getClass.getResource("/jbc/extract_link.html").getPath),
    "video_girl_1_manga" -> browser.parseFile(getClass.getResource("/jbc/extract_manga.html").getPath),
    "http://mangasjbc.uol.com.br/titulos/fairy-tail/page/1" -> browser.parseFile(getClass.getResource("/jbc/extract_page_1.html").getPath),
    "http://mangasjbc.uol.com.br/titulos/fairy-tail/page/2" -> browser.parseFile(getClass.getResource("/jbc/extract_page_2.html").getPath)
  )

  /** Get a document from a url.
    * @param url The url to get the document from.
    * @return An [[scala.Option]] of [[Document]]
    **/
  override def get(url: String): Option[Document] = urls.get(url)

}
