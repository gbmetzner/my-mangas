package com.gbm.mymangas.actors.scrapings

import com.gbm.mymangas.models.Manga
import net.ruippeixotog.scalascraper.browser.Browser
import org.jsoup.nodes.Document

import scala.util.{Failure, Success, Try}

/**
 * @author Gustavo Metzner on 10/17/15.
 */

trait Scrapable {
  val collection: String
  val latestNumber: Int
  val baseURL: String
  val searchParam: String

  private[scrapings] val browser = BrowserDecorator(new Browser)

  case class BrowserDecorator(private val browser: Browser) {
    def get(url: String): Option[Document] = Try(browser get url) match {
      case Success(document) => Some(document)
      case Failure(_) => None
    }
  }

  protected val extractNumberF: (Document) => Int

  private[scrapings] def extractManga(document: Document): Manga = {
    Manga(collection = collection, name = extractName(document), number = extractNumber(document)(extractNumberF))
  }

  private[scrapings] def extractMangasLinks(document: Document)(f: Document => Seq[String]): Seq[String] = {
    f(document).map(link => s"$baseURL$link")
  }

  def extractName(document: Document): String

  private def extractNumber(document: Document)(f: Document => Int): Int = Try(f(document)) match {
    case Success(number) => number
    case Failure(_) => 0
  }

  def extractImgLink(document: Document): String

  def scrape: Seq[(Manga, String)] = {
    val links = extractLinks

    @annotation.tailrec
    def process(links: Seq[String], acc: Seq[(Manga, String)]): Seq[(Manga, String)] = links match {
      case Nil => acc
      case head :: tail => browser get head match {
        case None => acc
        case Some(document) =>
          val manga = (extractManga(document), extractImgLink(document))
          process(tail, if (manga._1.number > latestNumber) acc :+ manga else acc)
      }
    }
    if (links.nonEmpty) process(links, Seq.empty[(Manga, String)]) else Seq.empty[(Manga, String)]
  }

  def extractLinks: Seq[String]
}