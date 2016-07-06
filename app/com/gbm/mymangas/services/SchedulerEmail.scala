package com.gbm.mymangas.services

import javax.inject.{ Inject, Singleton }

import akka.actor.ActorSystem
import com.gbm.mymangas.actors.emails.EmailSender
import com.typesafe.scalalogging.LazyLogging
import play.api.Application
import play.api.libs.mailer.MailerClient

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._

/**
 * Created by gbmetzner on 11/7/15.
 */
@Singleton
class SchedulerEmail @Inject() (
  val app: Application,
  val system: ActorSystem,
  mailerClient: MailerClient
)(implicit ec: ExecutionContext)
    extends LazyLogging {

  implicit val application = app

  logger info "Email scheduling..."

  val emailSender = system.actorOf(EmailSender.props(mailerClient), "email-sender")

  system.scheduler.schedule(5.hours, 24.hours, emailSender, EmailSender.Send)
}

