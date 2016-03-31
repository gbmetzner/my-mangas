package com.gbm.mymangas.modules

import com.gbm.mymangas.services.{SchedulerEmail, SchedulerMangaScraping}
import com.google.inject.AbstractModule
import com.typesafe.scalalogging.LazyLogging
import play.api.libs.concurrent.AkkaGuiceSupport

/**
  * @author Gustavo Metzner on 10/17/15.
  */
class OnStartApplication
  extends AbstractModule with AkkaGuiceSupport with LazyLogging {

  override def configure() {

    logger debug "On start application binding actors..."
    bind(classOf[SchedulerMangaScraping]).asEagerSingleton()
    bind(classOf[SchedulerEmail]).asEagerSingleton()
  }

}
