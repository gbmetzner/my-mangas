package com.gbm.mymangas.actors

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import com.gbm.mymangas.actors.PagesActor.messages.{FindPages, Pages}
import com.gbm.mymangas.scrapes.urls.{JBCPagesFinder, PagesFinder, PaniniPagesFinder}
import com.gbm.mymangas.utils.browser.DefaultBrowser
import org.jsoup.nodes.Document

/**
  * Created by gbmetzner on 11/19/15.
  */
object PagesActor {
  def props(creator: ActorRef): Props = Props(new PagesActor(creator))

  protected[actors] def pageFinder(publishersName: String): PagesFinder = publishersName match {
    case "JBC" => JBCPagesFinder(DefaultBrowser)
    case "Panini" => PaniniPagesFinder(DefaultBrowser)
  }

  object messages {

    case class FindPages(publishersName: String, searchParam: String, latestNumber: Int)

    case class Pages(searchParam: String, pages: Seq[Document])

  }

}

class PagesActor(creator: ActorRef) extends Actor with ActorLogging {

  override def receive: Receive = {
    case FindPages(publishersName, searchParam, latestNumber) =>
      val pages = PagesActor.pageFinder(publishersName).generate(searchParam, latestNumber)
      creator ! Pages(searchParam, pages)
  }

}
