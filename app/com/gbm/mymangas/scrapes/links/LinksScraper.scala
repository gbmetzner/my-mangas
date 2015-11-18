package com.gbm.mymangas.scrapes.links

import com.gbm.mymangas.scrapes.Scraper
import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.scraper.ContentExtractors._
import org.jsoup.nodes.Document

/** Defines a scraper for manga's links.
  * @author Gustavo Metzner on 11/15/15.
  */
trait LinksScraper extends Scraper {

  override type R = Seq[String]

  protected[links] def extract(document: Document)(f: Document => R): R = {
    f(document).map(link => s"$baseURL$link")
  }

  protected[this] sealed trait Scraper {
    def extractLinks(document: Document): R
  }

  object panini extends Scraper {
    override def extractLinks(document: Document): R = {
      Seq(document >> extractor("h3 a", attr("href")))
    }
  }

  object jbc extends Scraper {
    override def extractLinks(document: Document): R = {
      document >> extractor(".txt a", attrs("href"))
    }
  }

}
