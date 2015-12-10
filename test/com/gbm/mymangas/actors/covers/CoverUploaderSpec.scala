package com.gbm.mymangas.actors.covers

import akka.actor.Props
import akka.testkit.TestProbe
import com.gbm.mymangas.base.ActorUnitSpec
import com.gbm.mymangas.data.MangaDataProvider._
import com.gbm.mymangas.upload.FakeFileUploaderComponentImpl
import com.gbm.mymangas.utils.UUID

/**
  * Created by gbmetzner on 12/4/15.
  */
class CoverUploaderSpec extends ActorUnitSpec("CoverUploaderActorSystem") {

  "A CoverUpload" should "handler an Upload message" in {
    val coverManager = TestProbe()
    val coverUploader = system.actorOf(Props(new CoverUploader(coverManager.ref) with FakeFileUploaderComponentImpl), "CoverManagerActor")

    val manga = gimme("berserk1")

    val id = UUID.actorId(manga.collection)

    coverManager.send(coverUploader, CoverUploader.Upload(manga, "", id))

    coverManager.expectMsg(CoverUploader.UploadDone(manga.copy(publicLink = "done"), id))

  }

}
