package com.gbm.mymangas.services

import javax.inject.{Inject, Named, Singleton}

import akka.actor.{ActorRef, ActorSystem}
import com.gbm.mymangas.actors.emails.EmailSender
import com.typesafe.scalalogging.LazyLogging
import play.api.Application

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._

/**
  * Created by gbmetzner on 11/7/15.
  */
@Singleton
class SchedulerEmail @Inject()(val app: Application,
                               val system: ActorSystem,
                               @Named("email-sender") val emailSender: ActorRef)(implicit ec: ExecutionContext)
  extends LazyLogging {

  implicit val application = app

  logger info "Email scheduling..."

  system.scheduler.schedule(1.hour, 24.hours, emailSender, EmailSender.Send)
}

