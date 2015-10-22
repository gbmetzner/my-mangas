package com.gbm.mymangas.actors.scrapings


import com.gbm.mymangas.models.Manga
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

  override def extractNumber(document: Document): Int = {
    (document >> extractor("#int_content h2", text)).split("#").last.toInt
  }

  override def extractImgLink(document: Document): String = {
    document >> extractor("#int_content img", attr("src"))
  }

  def scrape: Seq[(Manga, String)] = {
    val links = extractLinks(searchParam)

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

  override def extractLinks(title: String): Seq[String] = {
    def buildPageURL(pageNumber: Int): String = s"http://mangasjbc.uol.com.br/titulos/$searchParam/page/$pageNumber"

    @annotation.tailrec
    def extract(page: Int, acc: Seq[String]): Seq[String] = browser get buildPageURL(page) match {
      case None => acc
      case Some(document) => extract(page + 1, acc ++ extractMangasLinks(document) {
        document => document >> extractor(".txt a", attrs("href"))
      })
    }

    extract(1, Seq.empty[String])
  }
}
