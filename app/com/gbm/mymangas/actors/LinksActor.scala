package com.gbm.mymangas.actors

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import com.gbm.mymangas.actors.LinksActor.{FindLinks, Links}
import com.gbm.mymangas.scrapes.links.{JBCLinksScraper, LinksScraper, PaniniLinksScraper}
import com.gbm.mymangas.utils.browser.DefaultBrowser
import org.jsoup.nodes.Document

/**
  * Created by gbmetzner on 11/21/15.
  */
object LinksActor {

  def props(creator: ActorRef): Props = Props(new LinksActor(creator))

  protected[actors] def linksFinder(publishersName: String): LinksScraper = publishersName match {
    case "JBC" => JBCLinksScraper(DefaultBrowser)
    case "Panini" => PaniniLinksScraper(DefaultBrowser)
  }

  case class FindLinks(publishersName: String, pages: Seq[Document])

  case class Links(links: Seq[Document])

}

class LinksActor(creator: ActorRef) extends Actor with ActorLogging {
  override def receive: Receive = {
    case FindLinks(publishersName, pages) =>
      val scraper = LinksActor.linksFinder(publishersName)
      creator ! Links(pages)
  }
}
