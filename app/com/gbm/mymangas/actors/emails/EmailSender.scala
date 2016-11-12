package com.gbm.mymangas.actors.emails

import akka.actor.{Actor, ActorLogging, Props}
import com.gbm.mymangas.models.Email
import com.gbm.mymangas.models.filters.MangaFilter
import com.gbm.mymangas.registries.MangaComponent
import com.gbm.mymangas.repositories.MangaRepository
import com.gbm.mymangas.services.impl.MangaService
import com.gbm.mymangas.utils.Config._
import org.joda.time.DateTime
import play.api.libs.mailer.MailerClient

import scala.concurrent.ExecutionContext.Implicits.global

/**
  * @author Gustavo Metzner on 11/7/15.
  */
object EmailSender {

  case object Send

  def props(mailerClient: MailerClient): Props = Props(new EmailSender(mailerClient, new MangaComponent(new MangaService, new MangaRepository)))

}

class EmailSender(mailerClient: MailerClient, mangaComponent: MangaComponent) extends Actor with ActorLogging {

  val mangaService: MangaService = mangaComponent.mangaService
  val mangaRepository: MangaRepository = mangaComponent.mangaRepository

  override def receive: Receive = {
    case EmailSender.Send =>
      val yesterday = DateTime.now().minusDays(1).withTimeAtStartOfDay
      val today = DateTime.now().withTimeAtStartOfDay

      log debug s"Finding between $yesterday and $today"

      val mangasF = mangaService.findBy(MangaFilter(from = Some(yesterday), to = Some(today)))(mangaRepository.findBy)

      mangasF.map(_.size).foreach {
        case size: Int if size > 0 => mailerClient.send(Email(to = getString("my.email"), from = getString("from.email"), qtyMangas = size))
        case 0 => log debug "No email to send."
      }
  }
}
