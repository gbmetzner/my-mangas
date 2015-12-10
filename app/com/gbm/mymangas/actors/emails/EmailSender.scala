package com.gbm.mymangas.actors.emails

import javax.inject.{Inject, Singleton}

import akka.actor.{Actor, ActorLogging}
import com.gbm.mymangas.models.Email
import com.gbm.mymangas.models.filters.MangaFilter
import com.gbm.mymangas.repositories.MangaRepositoryComponent
import com.gbm.mymangas.services.MangaServiceComponent
import com.gbm.mymangas.utils.Config._
import org.joda.time.DateTime
import play.api.libs.mailer.MailerClient

import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by gbmetzner on 11/7/15.
  */
object EmailSender {

  case object Send

}

@Singleton
class EmailSender @Inject()(mailerClient: MailerClient) extends Actor with ActorLogging {
  requires: MangaServiceComponent with MangaRepositoryComponent =>

  override def receive: Receive = {
    case EmailSender.Send =>
      val yesterday = DateTime.now().minusDays(1).withTimeAtStartOfDay
      val today = DateTime.now().withTimeAtStartOfDay

      log debug s"Finding between $yesterday and $today"

      val mangasF = mangaService.findBy(MangaFilter(from = Some(yesterday), to = Some(today)))(mangaRepository.findBy)

      mangasF.map(_.size).foreach {
        case size: Int if size > 0 =>
          mailerClient.send(Email(to = getString("my.email"), from = getString("from.email"), qtyMangas = size))
        case 0 => log debug "No email to send."
      }
  }
}
