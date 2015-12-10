package com.gbm.mymangas.actors

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import com.gbm.mymangas.actors.PagesActor.FindPages
import com.gbm.mymangas.models.Collection
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

  case class FindPages(collection: Collection, latestNumber: Int, id: String)

  case class Pages(collection: Collection, pages: Seq[Document], id: String)

}

class PagesActor(creator: ActorRef) extends Actor with ActorLogging {

  override def receive: Receive = {
    case FindPages(collection, latestNumber, id) =>
      val pages = PagesActor.pageFinder(collection.publisher).generate(collection.searchParam, latestNumber)
      creator ! PagesActor.Pages(collection, pages, id)
  }

}
