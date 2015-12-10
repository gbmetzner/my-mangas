package com.gbm.mymangas.actors.links

import akka.actor.Props
import akka.testkit.TestProbe
import com.gbm.mymangas.actors.LinksActor
import com.gbm.mymangas.base.ActorUnitSpec
import com.gbm.mymangas.browser.FakeBrowser
import com.gbm.mymangas.data.CollectionDataProvider
import com.gbm.mymangas.models.Collection
import com.gbm.mymangas.utils.UUID._

/**
  * Created by gbmetzner on 12/9/15.
  */
class LinksActorSpec extends ActorUnitSpec("LinksActorSystem") {
  "A LinksActor" should "react properly to FindLinks for Panini" in {

    val manager = TestProbe()

    val linksActor = system.actorOf(Props(new LinksActor(manager.ref, FakeBrowser)), "LinksActorPanini")

    val collection: Collection = CollectionDataProvider.gimme("berserk_1")

    val id = actorId(collection.name)

    val doc = FakeBrowser.get("berserk_links").get
    val docResult = FakeBrowser.get("http://www.paninicomics.com.br/web/guest/productDetail?viewItem=765014").get

    manager.send(linksActor, LinksActor.FindLinks(collection, doc, id))

    manager.expectMsg(LinksActor.Links(collection, List(docResult), id))
  }

  it should "react properly to FindLinks for JBC" in {

    val manager = TestProbe()

    val linksActor = system.actorOf(Props(new LinksActor(manager.ref, FakeBrowser)), "LinksActorJBC")

    val collection: Collection = CollectionDataProvider.gimme("video_girl_1")

    val id = actorId(collection.name)

    val doc = FakeBrowser.get("video_girl_links").get
    val docResult = FakeBrowser.get("http://www.paninicomics.com.br/web/guest/productDetail?viewItem=765014").get

    manager.send(linksActor, LinksActor.FindLinks(collection, doc, id))

    manager.expectMsgPF() {
      case LinksActor.Links(c, list, i) =>
        c shouldBe collection
        list.size shouldBe 10
        i shouldBe id
    }
  }

}
