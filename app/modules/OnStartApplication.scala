package modules

import actors.collections.CollectionManager
import actors.mangas.MangaManager
import com.google.inject.AbstractModule
import play.api.libs.concurrent.AkkaGuiceSupport
import services.SchedulerMangaScraping

/**
 * @author Gustavo Metzner on 10/17/15.
 */
class OnStartApplication extends AbstractModule with AkkaGuiceSupport {
  override def configure(): Unit = {
    bindActor[CollectionManager]("collection-manager")
    bindActor[MangaManager]("manga-manager")
    bind(classOf[SchedulerMangaScraping]).asEagerSingleton()
  }
}
