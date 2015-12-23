package com.gbm.mymangas.services.manga

import java.io.File
import java.util.UUID

import com.gbm.mymangas.base.UnitSpec
import com.gbm.mymangas.models.Manga
import com.gbm.mymangas.models.filters.Predicate
import com.gbm.mymangas.services.MangaServiceComponent
import com.gbm.mymangas.services.impl.MangaServiceComponentImpl
import com.gbm.mymangas.upload.FakeFileUploaderComponentImpl
import com.gbm.mymangas.utils.messages.{Warning, Error, Succeed}
import reactivemongo.api.commands.{DefaultWriteResult, LastError, WriteConcernError, WriteError}

import scala.concurrent.Future

/**
  * Created by gbmetzner on 12/22/15.
  */
class MangaServiceSpec extends UnitSpec {

  val dragonBall = Manga(collection = "Dragon Ball", number = 1)
  val error = WriteConcernError(1, "error")
  val actionOk = LastError(true, None, None, None, 1, None, false, None, None, false, None, None)
  val actionNOk = DefaultWriteResult(false, 1, Seq.empty[WriteError], Some(error), None, None)
  val fWriteResultInsertOk = (m: Manga) => Future.successful(actionOk)
  val fWriteResultInsertNOk = (m: Manga) => Future.successful(actionNOk)
  val fWriteResultUpdateOk = (id: UUID, m: Manga) => Future.successful(actionOk)
  val fWriteResultUpdateNOk = (id: UUID, m: Manga) => Future.successful(actionNOk)
  val fWriteResultRemoveOk = (id: UUID) => Future.successful(actionOk)
  val fWriteResultRemoveNOk = (id: UUID) => Future.successful(actionNOk)
  val findNone = (p: Predicate) => Future.successful(None)
  val findOne = (p: Predicate) => Future.successful(Some(dragonBall))

  "A MangaService" should "insert a manga correctly" in {
    val mangaServiceComponent = new MangaServiceComponent with MangaServiceComponentImpl with FakeFileUploaderComponentImpl

    val result = mangaServiceComponent.mangaService.insert(dragonBall)(fWriteResultInsertOk)(findNone)

    result.futureValue shouldBe Right(Succeed("manga.added"))
  }

  it should "not insert a manga correctly due an error" in {
    val mangaServiceComponent = new MangaServiceComponent with MangaServiceComponentImpl with FakeFileUploaderComponentImpl


    val result = mangaServiceComponent.mangaService.insert(dragonBall)(fWriteResultInsertNOk)(findNone)

    result.futureValue shouldBe Left(Error("error.general"))
  }

  it should "not insert a manga correctly due an existent manga in db" in {
    val mangaServiceComponent = new MangaServiceComponent with MangaServiceComponentImpl with FakeFileUploaderComponentImpl

    val result = mangaServiceComponent.mangaService.insert(dragonBall)(fWriteResultInsertOk)(findOne)

    result.futureValue shouldBe Left(Error("manga.already.exists"))
  }

  it should "update a manga correctly" in {
    val mangaServiceComponent = new MangaServiceComponent with MangaServiceComponentImpl with FakeFileUploaderComponentImpl

    val result = mangaServiceComponent.mangaService.update(dragonBall.id, dragonBall)(fWriteResultUpdateOk)(findOne)

    result.futureValue shouldBe Right(Succeed("manga.updated"))
  }

  it should "not update a manga correctly due an error" in {
    val mangaServiceComponent = new MangaServiceComponent with MangaServiceComponentImpl with FakeFileUploaderComponentImpl

    val result = mangaServiceComponent.mangaService.update(dragonBall.id, dragonBall)(fWriteResultUpdateNOk)(findOne)

    result.futureValue shouldBe Left(Error("error.general"))
  }

  it should "not update a manga correctly due a manga not found" in {
    val mangaServiceComponent = new MangaServiceComponent with MangaServiceComponentImpl with FakeFileUploaderComponentImpl

    val result = mangaServiceComponent.mangaService.update(dragonBall.id, dragonBall)(fWriteResultUpdateOk)(findNone)

    result.futureValue shouldBe Left(Warning("manga.not.found"))
  }

  it should "remove a manga correctly" in {
    val mangaServiceComponent = new MangaServiceComponent with MangaServiceComponentImpl with FakeFileUploaderComponentImpl

    val result = mangaServiceComponent.mangaService.remove(dragonBall.id)(fWriteResultRemoveOk)(findOne)

    result.futureValue shouldBe Right(Succeed("manga.removed"))
  }

  it should "not remove a manga correctly due an error" in {
    val mangaServiceComponent = new MangaServiceComponent with MangaServiceComponentImpl with FakeFileUploaderComponentImpl

    val result = mangaServiceComponent.mangaService.remove(dragonBall.id)(fWriteResultRemoveNOk)(findOne)

    result.futureValue shouldBe Left(Error("error.general"))
  }

  it should "not remove a manga correctly due a manga not found" in {
    val mangaServiceComponent = new MangaServiceComponent with MangaServiceComponentImpl with FakeFileUploaderComponentImpl

    val result = mangaServiceComponent.mangaService.remove(dragonBall.id)(fWriteResultRemoveOk)(findNone)

    result.futureValue shouldBe Left(Error("manga.not.found"))
  }

  it should "update the ownership correctly" in {
    val mangaServiceComponent = new MangaServiceComponent with MangaServiceComponentImpl with FakeFileUploaderComponentImpl

    val result = mangaServiceComponent.mangaService.updateOwnership(dragonBall.id, true)(fWriteResultUpdateOk)(findOne)

    result.futureValue shouldBe  Right(Succeed("manga.updated"))
  }

  it should "not update the ownership correctly due a manga not found" in {
    val mangaServiceComponent = new MangaServiceComponent with MangaServiceComponentImpl with FakeFileUploaderComponentImpl

    val result = mangaServiceComponent.mangaService.updateOwnership(dragonBall.id, true)(fWriteResultUpdateOk)(findNone)

    result.futureValue shouldBe Left(Warning("manga.not.found"))
  }

  it should "not update the ownership correctly due a db error" in {
    val mangaServiceComponent = new MangaServiceComponent with MangaServiceComponentImpl with FakeFileUploaderComponentImpl

    val result = mangaServiceComponent.mangaService.updateOwnership(dragonBall.id, true)(fWriteResultUpdateNOk)(findOne)

    result.futureValue shouldBe  Left(Error("error.general"))
  }

  it should "return the latest edition from a collection's name" in {
    val mangaServiceComponent = new MangaServiceComponent with MangaServiceComponentImpl with FakeFileUploaderComponentImpl

    val result = mangaServiceComponent.mangaService.latestNumber(dragonBall.collection)(findOne)

    result.futureValue.get.number shouldBe 1
  }

  it should "return no latest edition from a collection's name" in {
    val mangaServiceComponent = new MangaServiceComponent with MangaServiceComponentImpl with FakeFileUploaderComponentImpl

    val result = mangaServiceComponent.mangaService.latestNumber(dragonBall.collection)(findNone)

    result.futureValue shouldBe None
  }

  it should "updload a cover for a manga" in {
    val mangaServiceComponent = new MangaServiceComponent with MangaServiceComponentImpl with FakeFileUploaderComponentImpl

    val result = mangaServiceComponent.mangaService.uploadCover(dragonBall.id, "/mangas/dragon_ball", new File(""))(fWriteResultUpdateOk)(findOne)

    result.futureValue shouldBe  Right(Succeed("manga.updated"))
  }

  it should "not updload a cover for a manga due a manga not found" in {
    val mangaServiceComponent = new MangaServiceComponent with MangaServiceComponentImpl with FakeFileUploaderComponentImpl

    val result = mangaServiceComponent.mangaService.uploadCover(dragonBall.id, "/mangas/dragon_ball", new File(""))(fWriteResultUpdateOk)(findNone)

    result.futureValue shouldBe Left(Warning("manga.not.found"))
  }

}
