package com.gbm.mymangas.actors.scrapings

import com.gbm.mymangas.models.Manga
import net.ruippeixotog.scalascraper.browser.Browser
import org.jsoup.nodes.Document

import scala.annotation.tailrec
import scala.util.{Failure, Success, Try}

/**
 * @author Gustavo Metzner on 10/17/15.
 */

sealed trait Scrapable {
  val collection: String
  val latestNumber: Int
  val baseURL: String

  private[scrapings] val browser = BrowserDecorator(new Browser)

  case class BrowserDecorator(private val browser: Browser) {
    def get(url: String): Option[Document] = Try(browser get url) match {
      case Success(document) => Some(document)
      case Failure(_) => None
    }
  }

  private[scrapings] def extractManga(document: Document): Manga = {
    Manga(collection = collection, name = extractName(document), number = extractNumber(document))
  }

  def extractName(document: Document): String

  def extractNumber(document: Document): Int

  def extractImgLink(document: Document): String

  def scrape: Seq[(Manga, String)]

}

import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.dsl.DSL._

class JBCScrapable(override val collection: String,
                            override val latestNumber: Int,
                            override val baseURL: String) extends Scrapable {


  override def extractName(document: Document): String = {
    document >> extractor(".excerpt", text)
  }

  override def extractNumber(document: Document): Int = {
    (document >> extractor("#int_content h2", text)).split("#").last.toInt
  }

  override def extractImgLink(document: Document): String = {
    document >> extractor("#int_content img", attr("src"))
  }

  def scrape: Seq[(Manga, String)] = {
    @tailrec
    def process(n: Int, acc: Seq[(Manga, String)]): Seq[(Manga, String)] = browser get f"$baseURL-$n%02d" match {
      case None => acc
      case Some(doc) =>
        val manga = extractManga(doc)
        val coverLink = extractImgLink(doc)
        process(n + 1, acc :+(manga, coverLink))
    }
    process(latestNumber + 1, Seq.empty[(Manga, String)])
  }

}
