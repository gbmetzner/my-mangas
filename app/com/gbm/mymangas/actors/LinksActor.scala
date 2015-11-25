package com.gbm.mymangas.actors

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import com.gbm.mymangas.actors.LinksActor.messages.{Links, FindLinks}
import com.gbm.mymangas.scrapes.links.{JBCLinksScraper, LinksScraper, PaniniLinksScraper}
import org.jsoup.nodes.Document
import play.api.libs.ws.WS

/**
  * Created by gbmetzner on 11/21/15.
  */
object LinksActor {

  WS.client

  def props(creator: ActorRef): Props = Props(new LinksActor(creator))

  protected[actors] def linksFinder(publishersName: String): LinksScraper = publishersName match {
    case "JBC" => JBCLinksScraper
    case "Panini" => PaniniLinksScraper
  }

  object messages {

    case class FindLinks(publishersName: String, pages: Seq[Document])

    case class Links(links: Seq[String])

  }

}

class LinksActor(creator: ActorRef) extends Actor with ActorLogging {
  override def receive: Receive = {
    case FindLinks(publishersName, pages) =>
      val scraper = LinksActor.linksFinder(publishersName)
      creator ! Links(pages.flatMap(scraper.scrape))
  }
}
