package com.gbm.mymangas.actors.covers

import akka.testkit.TestProbe
import com.gbm.mymangas.base.ActorUnitSpec
import com.gbm.mymangas.data.MangaDataProvider._
import com.gbm.mymangas.upload.FakeFileUploader

/**
  * Created by gbmetzner on 12/4/15.
  */
class CoverUploaderSpec extends ActorUnitSpec("CoverUploaderActorSystem") {

  "A CoverUpload" should "handler an Upload message" in {
    val coverManager = TestProbe()
    val coverUploader = system.actorOf(CoverUploader.props(coverManager.ref, FakeFileUploader), "CoverManagerActor")

    val manga = gimme("berserk1")

    coverManager.send(coverUploader, CoverUploader.Upload(manga, ""))

    coverManager.expectMsg(CoverUploader.UploadDone(manga.copy(publicLink = "done")))

  }

}
