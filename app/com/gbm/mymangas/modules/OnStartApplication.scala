package com.gbm.mymangas.modules

import com.gbm.mymangas.actors.collections.CollectionManager
import com.gbm.mymangas.actors.mangas.MangaManager
import com.google.inject.AbstractModule
import com.typesafe.scalalogging.LazyLogging
import play.api.libs.concurrent.AkkaGuiceSupport
import com.gbm.mymangas.services.SchedulerMangaScraping

/**
 * @author Gustavo Metzner on 10/17/15.
 */
class OnStartApplication extends AbstractModule with AkkaGuiceSupport with LazyLogging {

  override def configure(): Unit = {
    logger debug "On start application binding actors..."

    bindActor[CollectionManager]("collection-manager")
    bindActor[MangaManager]("manga-manager")
    bind(classOf[SchedulerMangaScraping]).asEagerSingleton()
  }

}
