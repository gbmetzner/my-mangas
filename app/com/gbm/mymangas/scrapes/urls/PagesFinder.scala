package com.gbm.mymangas.scrapes.urls

import com.gbm.mymangas.utils.browser.Browser
import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.scraper.ContentExtractors._
import org.jsoup.nodes.Document

import scala.util.{Failure, Success, Try}

/**
  * Created by gbmetzner on 11/17/15.
  */
trait PagesFinder {
  val baseURL: String
  val browser: Browser

  def generate(searchParam: String, latestNumber: Int): Seq[Document]

  protected[urls] def generateURLs(searchParam: String, latestNumber: Int, page: Int)
                                  (f: (String, Int) => Option[Document])
                                  (g: (Document, Int) => Boolean): Seq[Document] = {
    @annotation.tailrec
    def generateURLs(page: Int, acc: Seq[Document]): Seq[Document] = f(searchParam, page) match {
      case Some(document) =>
        if (g(document, latestNumber)) generateURLs(page + 1, acc :+ document)
        else acc
      case None => acc
    }
    generateURLs(page + 1, Seq.empty[Document])
  }


  object panini {

    def verifyLatestEdition(document: Document, latestNumber: Int): Boolean = {
      Try((document >> extractor(".search_summary h3 strong", text)).trim().toInt) match {
        case Success(edition) => edition > 0
        case Failure(_) => false
      }
    }

    def docFromPage(searchParam: String, page: Int): Option[Document] = {
      val url = s"$baseURL/web/guest/search_product?search=$searchParam".replace("#", page.toString)
      browser.get(url)
    }
  }

  object jbc {

    def verifyLatestEdition(document: Document, latestNumber: Int): Boolean = {
      (document >> extractor(".txt a strong", text)).split("#").last.trim().toInt > latestNumber
    }

    def docFromPage(searchParam: String, page: Int): Option[Document] = {
      val url = s"${baseURL}titulos/$searchParam/page/$page"
      browser.get(url)
    }
  }

}
