package com.gbm.mymangas.actors.scrapings

import com.typesafe.scalalogging.LazyLogging
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.dsl.DSL._
import org.jsoup.nodes.Document

/**
 * @author Gustavo Metzner on 10/22/15.
 */
class PaniniScraping(override val collection: String,
                     override val latestNumber: Int,
                     override val searchParam: String) extends Scrapable with LazyLogging {

  override val baseURL: String = "http://www.paninicomics.com.br"

  override def extractNumber(document: Document): Int = {
    (document >> extractor(".title h3", text)).toLowerCase.split("ed.").last.trim().toInt
  }

  override def extractLinks: Seq[String] = {

    def extractQuantity(document: Document): Int = {
      (document >> extractor(".search_summary h3 strong", text)).toInt
    }
    def buildPageURL(pageNumber: Int): String = {
      s"$baseURL/web/guest/search_product?search=$searchParam".replace("#", pageNumber.toString)
    }

    @annotation.tailrec
    def extract(page: Int, acc: Seq[String]): Seq[String] = browser get buildPageURL(page) match {
      case None => acc
      case Some(document) =>
        if (extractQuantity(document) > 0) extract(page + 1, acc ++ extractMangasLinks(document) {
          document => Seq(document >> extractor("h3 a", attr("href")))
        })
        else acc
    }

    extract(latestNumber + 1, Seq.empty[String])
  }

  override def extractImgLink(document: Document): String = {
    s"$baseURL${document >> extractor(".cover img", attr("src"))}"
  }

  override def extractName(document: Document): String = {
    (document >> extractor(".titles h1", text)).split("-").head.trim()
  }
}
