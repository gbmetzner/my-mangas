package com.gbm.mymangas.actors.scrapings

import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.dsl.DSL._
import org.jsoup.nodes.Document

/**
  * @author Gustavo Metzner on 10/17/15.
  */
class FairyTailScraping(override val collection: String,
                        override val latestNumber: Int,
                        override val searchParam: String) extends JBCScraping(collection, latestNumber, searchParam) {


  override protected val extractNumberF: (Document) => Int = {
    document => (document >> extractor(".txt h1", text)).split("#").last.toInt
  }

  override def extractImgLink(document: Document): String = document >> extractor(".imgCapa", attr("src"))

  override def buildPageURL(pageNumber: Int): String = s"$baseURL$searchParam-$pageNumber"

  override def extractLinks: Seq[String] = {
    @annotation.tailrec
    def extract(page: Int, acc: Seq[String]): Seq[String] = browser get buildPageURL(page) match {
      case None => acc
      case Some(document) =>
        extract(page + 1, acc ++ Seq(buildPageURL(page)))
    }
    extract(latestNumber + 1, Seq.empty[String])
  }
}
