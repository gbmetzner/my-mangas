package com.gbm.mymangas.scrapes.mangas

import com.gbm.mymangas.models.Manga
import com.gbm.mymangas.scrapes.Scraper
import com.gbm.mymangas.utils.StandardizeNames._
import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.model.Document
import net.ruippeixotog.scalascraper.scraper.ContentExtractors._

/**
 * @author Gustavo Metzner on 11/15/15.
 */
trait MangaScraper extends Scraper {

  override type R = (Manga, String)

  protected[mangas] def extract(document: Document)(f: Document => Manga)(g: Document => String): R = {
    (f(document), g(document))
  }

  protected[this] sealed trait Scraper {

    def extractManga(document: Document): Manga = {
      val collection = extractCollection(document).capitalizeAll
      val number = extractNumber(document)
      Manga(collection = collection, number = number)
    }

    protected def extractCoverLink(document: Document): String

    protected[this] def extractNumber(document: Document): Int

    protected[this] def extractCollection(document: Document): String
  }

  object Panini extends Scraper {

    override def extractCoverLink(document: Document): String = {
      s"$baseURL${document >> extractor(".cover img", attr("src"))}"
    }

    protected[this] override def extractNumber(document: Document): Int = {
      (document >> extractor(".title h3", text)).toLowerCase.split("ed.").last.trim().toInt
    }

    protected[this] override def extractCollection(document: Document): String = {
      (document >> extractor(".titles h1", text)).split("-").head.trim()
    }
  }

  object JBC extends Scraper {

    override def extractCoverLink(document: Document): String = {
      document >> extractor(".content img", attr("src"))
    }

    protected[this] override def extractNumber(document: Document): Int = {
      (document >> extractor(".content h1", text)).split("#").last.trim().toInt
    }

    protected[this] override def extractCollection(document: Document): String = {
      (document >> extractor(".content h1", text)).split("#").head.trim()
    }
  }

}
