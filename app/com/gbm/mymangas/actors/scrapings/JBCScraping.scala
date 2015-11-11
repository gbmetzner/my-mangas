package com.gbm.mymangas.actors.scrapings


import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.dsl.DSL._
import org.jsoup.nodes.Document

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

  def buildPageURL(pageNumber: Int): String = s"${baseURL}titulos/$searchParam/page/$pageNumber"

  override def extractLinks: Seq[String] = {

    @annotation.tailrec
    def extract(page: Int, acc: Seq[String]): Seq[String] = browser get buildPageURL(page) match {
      case None => acc
      case Some(document) => extract(page + 1, acc ++ extractMangasLinks(document)(extractLinks))
    }

    extract(latestNumber + 1, Seq.empty[String])
  }

  protected[scrapings] def extractLinks(document: Document): Seq[String] = document >> extractor(".txt a", attrs("href"))

}
