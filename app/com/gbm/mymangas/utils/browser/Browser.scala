package com.gbm.mymangas.utils.browser

import net.ruippeixotog.scalascraper.model.Document

import scala.util.Try

/**
 * Defines how to retrieve documents.
 * It is a trait to make it easier to test.
 * @author Gustavo Metzner on 11/15/15.
 */
trait Browser {

  /**
   * Get a document from a url.
   * @param url The url to get the document from.
   * @return An [[scala.Option]] of [[org.jsoup.nodes.Document]]
   */
  def get(url: String): Option[Document]
}

/** The default implementation */
object DefaultBrowser extends Browser {

  import net.ruippeixotog.scalascraper.browser.JsoupBrowser

  /** A browser to get the document. */
  private val browser = new JsoupBrowser

  /**
   * Get a document from a url.
   * @param url The url to get the document from.
   * @return An [[scala.Option]] of [[org.jsoup.nodes.Document]]
   */
  override def get(url: String): Option[Document] = Try(browser get url).toOption
}
