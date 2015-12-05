package com.gbm.mymangas.actors.covers

import java.io.FileNotFoundException

import akka.testkit.TestProbe
import com.gbm.mymangas.base.ActorUnitSpec
import com.gbm.mymangas.data.MangaDataProvider._
import com.gbm.mymangas.download.FakeFileDownloader
import com.gbm.mymangas.utils.files.download.FileDownloader

/**
  * Created by gbmetzner on 12/5/15.
  */
class CoverDownloaderSpec extends ActorUnitSpec("CoverDownloaderActorSystem") {

  "A CoverDownloader" should "react properly to Download/DownloadDone" in {
    val coverManager = TestProbe()
    val coverDownloader = system.actorOf(CoverDownloader.props(coverManager.ref, FakeFileDownloader), "CoverManagerActor")

    val manga = gimme("berserk1")

    coverManager.send(coverDownloader, CoverDownloader.Download(manga, "url"))

    coverManager.expectMsg(CoverDownloader.DownloadDone(manga, "/tmp/berserk_1.jpg"))
  }

  it should "react properly to Download/CoverNotAvailable" in {

    val fakeFileDownloader = new FileDownloader {
      override def downloadImage(url: String, file: String): String = throw new FileNotFoundException("Download Error")

      override def extractExtension(url: String): String = ".jpg"
    }

    val coverManager = TestProbe()
    val coverDownloader = system.actorOf(CoverDownloader.props(coverManager.ref, fakeFileDownloader), "CoverManagerActorError")

    val manga = gimme("berserk1")

    coverManager.send(coverDownloader, CoverDownloader.Download(manga, "url"))

    coverManager.expectMsg(CoverDownloader.CoverNotAvailable(manga.copy(publicLink = "manga.no.cover")))
  }

}
