package com.gbm.mymangas.actors.scrapings


import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.dsl.DSL._
import org.jsoup.nodes.Document

import scala.util.{Failure, Success, Try}

/**
 * @author Gustavo Metzner on 10/22/15.
 */

class JBCScraping(override val collection: String,
                  override val latestNumber: Int,
                  override val searchParam: String) extends Scrapable {

  override val baseURL: String = "http://mangasjbc.uol.com.br/"

  override def extractName(document: Document): String = {
    document >> extractor(".excerpt", text)
  }

  override protected val extractNumberF: (Document) => Int = {
    document => (document >> extractor("#int_content h2", text)).split("#").last.toInt
  }

  override def extractImgLink(document: Document): String = {
    document >> extractor("#int_content img", attr("src"))
  }

  override def extractLinks: Seq[String] = {

    def buildPageURL(pageNumber: Int): String = s"http://mangasjbc.uol.com.br/titulos/$searchParam/page/$pageNumber"

    @annotation.tailrec
    def extract(page: Int, acc: Seq[String]): Seq[String] = browser get buildPageURL(page) match {
      case None => acc
      case Some(document) => extract(page + 1, acc ++ extractMangasLinks(document) {
        document => document >> extractor(".txt a", attrs("href"))
      })
    }

    extract(latestNumber + 1, Seq.empty[String])
  }

}
