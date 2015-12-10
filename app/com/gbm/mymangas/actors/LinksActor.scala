package com.gbm.mymangas.actors

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import com.gbm.mymangas.actors.LinksActor.{FindLinks, Links}
import com.gbm.mymangas.models.Collection
import com.gbm.mymangas.scrapes.links.{JBCLinksScraper, LinksScraper, PaniniLinksScraper}
import com.gbm.mymangas.utils.browser.Browser
import org.jsoup.nodes.Document

/**
  * Created by gbmetzner on 11/21/15.
  */
object LinksActor {

  def props(creator: ActorRef, browser: Browser): Props = Props(new LinksActor(creator, browser))

  protected[actors] def linksFinder(publishersName: String, browser: Browser): LinksScraper = publishersName match {
    case "JBC" => JBCLinksScraper(browser)
    case "Panini" => PaniniLinksScraper(browser)
  }

  case class FindLinks(collection: Collection, pages: Document, id: String)

  case class Links(collection: Collection, links: Seq[Document], id: String)

}

class LinksActor(creator: ActorRef, browser: Browser) extends Actor with ActorLogging {

  override def receive: Receive = {
    case FindLinks(collection, page, id) =>
      val scraper = LinksActor.linksFinder(collection.publisher, browser)
      val links = scraper.scrape(page)
      creator ! Links(collection, links, id)
  }

}
