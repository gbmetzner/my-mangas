package com.gbm.mymangas.actors.mangas

import akka.actor.Props
import akka.testkit.TestProbe
import com.gbm.mymangas.actors.scrapings.ScrapeMangaActor
import com.gbm.mymangas.base.ActorUnitSpec
import com.gbm.mymangas.browser.FakeBrowser
import com.gbm.mymangas.data.CollectionDataProvider
import com.gbm.mymangas.models.Collection
import com.gbm.mymangas.utils.UUID._

/**
  * Created by gbmetzner on 12/10/15.
  */
class ScrapeMangaActorSpec extends ActorUnitSpec("ScrapeMangaActorSystem") {

  "A ScrapeMangaActor" should "react to Scrape for Panini" in {
    val manager = TestProbe()

    val scrapeMangaActor = system.actorOf(Props(new ScrapeMangaActor(manager.ref)), "ScrapeMangaActorPanini")

    val collection: Collection = CollectionDataProvider.gimme("berserk_1")

    val id = actorId(collection.name)

    val doc = FakeBrowser.get("http://www.paninicomics.com.br/web/guest/productDetail?viewItem=765014").get

    manager.send(scrapeMangaActor, ScrapeMangaActor.Scrape(collection, doc, id))

    manager.expectMsgPF() {
      case ScrapeMangaActor.ScrapeDone(manga, url, i) =>
        manga.fullName shouldBe "Berserk - #1"
        url shouldBe "http://www.paninicomics.com.br/image/image_gallery?img_id=7295645&t=1410887306210"
        i shouldBe id
    }
  }


  it should "react to Scrape for JBC" in {
    val manager = TestProbe()

    val scrapeMangaActor = system.actorOf(Props(new ScrapeMangaActor(manager.ref)), "ScrapeMangaActorJBC")

    val collection: Collection = CollectionDataProvider.gimme("video_girl_1")

    val id = actorId(collection.name)

    val doc = FakeBrowser.get("video_girl_1_manga").get

    manager.send(scrapeMangaActor, ScrapeMangaActor.Scrape(collection, doc, id))

    manager.expectMsgPF() {
      case ScrapeMangaActor.ScrapeDone(manga, url, i) =>
        manga.fullName shouldBe "Video Girl - #1"
        url shouldBe "http://jbchost.com.br/mangasjbc/wp-content/uploads/2005/05/capa_video_girl_ai_01_g.jpg"
        i shouldBe id
    }
  }

}
