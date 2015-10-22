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

  override def extractNumber(document: Document): Int = (document >> extractor(".txt h1", text)).split("#").last.toInt

  override def extractImgLink(document: Document): String = document >> extractor(".imgCapa", attr("src"))

}
