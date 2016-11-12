package com.gbm.mymangas.services.publisher

import java.util.UUID

import com.gbm.mymangas.base.UnitSpec
import com.gbm.mymangas.models.Publisher
import com.gbm.mymangas.models.filters.Predicate
import com.gbm.mymangas.services.PublisherService
import com.gbm.mymangas.utils.messages.{Error, Succeed}
import reactivemongo.api.commands._

import scala.concurrent.Future

/**
 * Created by gbmetzner on 12/18/15.
 */
class PublisherServiceSpec extends UnitSpec {

  val panini = Publisher(name = "Panini")
  val error = WriteConcernError(1, "error")
  val actionOk = LastError(true, None, None, None, 1, None, false, None, None, false, None, None)
  val actionNOk = DefaultWriteResult(false, 1, Seq.empty[WriteError], Some(error), None, None)
  val fWriteResultInsertOk = (p: Publisher) => Future.successful(actionOk)
  val fWriteResultInsertNOk = (p: Publisher) => Future.successful(actionNOk)
  val fWriteResultUpdateOk = (id: UUID, p: Publisher) => Future.successful(actionOk)
  val fWriteResultUpdateNOk = (id: UUID, p: Publisher) => Future.successful(actionNOk)
  val fWriteResultRemoveOk = (id: UUID) => Future.successful(actionOk)
  val fWriteResultRemoveNOk = (id: UUID) => Future.successful(actionNOk)
  val findNone = (p: Predicate) => Future.successful(None)
  val findOne = (p: Predicate) => Future.successful(Some(panini))
  val findNoneList = (p: Predicate) => Future.successful(Nil)
  val findOneList = (p: Predicate) => Future.successful(List(panini))

  "A PublisherService" should "insert a publisher correctly" in {
    val publisherService = new PublisherService

    val result = publisherService.insert(panini)(fWriteResultInsertOk)(findNone)

    result.futureValue shouldBe Right(Succeed("publisher.added"))
  }

  it should "not insert a publisher correctly due an error" in {
    val publisherService = new PublisherService

    val result = publisherService.insert(panini)(fWriteResultInsertNOk)(findNone)

    result.futureValue shouldBe Left(Error("error.general"))
  }

  it should "not insert a publisher correctly due an existent publisher in db" in {
    val publisherService = new PublisherService

    val result = publisherService.insert(panini)(fWriteResultInsertOk)(findOne)

    result.futureValue shouldBe Left(Error("publisher.already.exists"))
  }

  it should "update a publisher correctly" in {
    val publisherService = new PublisherService

    val result = publisherService.update(panini.id, panini)(fWriteResultUpdateOk)(findOneList)

    result.futureValue shouldBe Right(Succeed("publisher.updated"))
  }

  it should "not update a publisher correctly due an error" in {
    val publisherService = new PublisherService

    val result = publisherService.update(panini.id, panini)(fWriteResultUpdateNOk)(findOneList)

    result.futureValue shouldBe Left(Error("error.general"))
  }

  it should "not update a publisher correctly due a publisher not found" in {
    val publisherService = new PublisherService

    val result = publisherService.update(panini.id, panini)(fWriteResultUpdateOk)(findNoneList)

    result.futureValue shouldBe Left(Error("publisher.not.found"))
  }

  it should "remove a publisher correctly" in {
    val publisherService = new PublisherService

    val result = publisherService.remove(panini.id)(fWriteResultRemoveOk)(findOne)

    result.futureValue shouldBe Right(Succeed("publisher.removed"))
  }

  it should "not remove a publisher correctly due an error" in {
    val publisherService = new PublisherService

    val result = publisherService.remove(panini.id)(fWriteResultRemoveNOk)(findOne)

    result.futureValue shouldBe Left(Error("error.general"))
  }

  it should "not remove a publisher correctly due a publisher not found" in {
    val publisherService = new PublisherService

    val result = publisherService.remove(panini.id)(fWriteResultRemoveOk)(findNone)

    result.futureValue shouldBe Left(Error("publisher.not.found"))
  }

}
