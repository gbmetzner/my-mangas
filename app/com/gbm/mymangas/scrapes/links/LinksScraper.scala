package com.gbm.mymangas.scrapes.links

import com.gbm.mymangas.scrapes.Scraper
import com.gbm.mymangas.utils.browser.Browser
import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.scraper.ContentExtractors._
import net.ruippeixotog.scalascraper.model.Document

/**
 * Defines a scraper for manga's links.
 *
 * @author Gustavo Metzner on 11/15/15.
 */
trait LinksScraper extends Scraper {

  val browser: Browser

  override type R = Seq[Document]
  type L = Seq[String]

  protected[links] def extract(document: Document)(f: Document => L): Seq[Document] = {
    f(document).map {
      link => browser.get(s"$baseURL$link")
    }.filter(_.isDefined).map(_.get)
  }

  protected[this] sealed trait Scraper {
    def extractLinks(document: Document): L
  }

  object Panini extends Scraper {
    override def extractLinks(document: Document): L = {
      Seq(document >> extractor("h3 a", attr("href")))
    }
  }

  object JBC extends Scraper {
    override def extractLinks(document: Document): L = {
      (document >> extractor(".edicoes li a", attrs("href"))).toSeq
    }
  }

}
