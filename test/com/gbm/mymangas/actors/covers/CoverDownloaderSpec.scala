package com.gbm.mymangas.actors.covers

import akka.actor.Props
import akka.testkit.TestProbe
import com.gbm.mymangas.base.ActorUnitSpec
import com.gbm.mymangas.data.MangaDataProvider._
import com.gbm.mymangas.actors.covers.download.{FakeFileDownloadComponentImpl, FakeFileDownloadComponentImplError}
import com.gbm.mymangas.utils.UUID

/**
  * Created by gbmetzner on 12/5/15.
  */
class CoverDownloaderSpec extends ActorUnitSpec("CoverDownloaderActorSystem") {

  "A CoverDownloader" should "react properly to Download/DownloadDone" in {
    val coverManager = TestProbe()
    val coverDownloader = system.actorOf(Props(new CoverDownloader(coverManager.ref) with FakeFileDownloadComponentImpl), "CoverManagerActor")

    val manga = gimme("berserk1")

    val id = UUID.actorId(manga.collection)

    coverManager.send(coverDownloader, CoverDownloader.Download(manga, "url", id))

    coverManager.expectMsg(CoverDownloader.DownloadDone(manga, "/tmp/berserk_1.jpg", id))
  }

  it should "react properly to Download/CoverNotAvailable" in {

    val coverManager = TestProbe()
    val coverDownloader = system.actorOf(Props(new CoverDownloader(coverManager.ref) with FakeFileDownloadComponentImplError), "CoverManagerActorError")

    val manga = gimme("berserk1")

    val id = UUID.actorId(manga.collection)

    coverManager.send(coverDownloader, CoverDownloader.Download(manga, "url", id))

    coverManager.expectMsg(CoverDownloader.CoverNotAvailable(manga, id))
  }

}
