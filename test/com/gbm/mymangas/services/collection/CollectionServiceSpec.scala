package com.gbm.mymangas.services.collection

import java.util.UUID

import com.gbm.mymangas.base.UnitSpec
import com.gbm.mymangas.models.Collection
import com.gbm.mymangas.models.filters.Predicate
import com.gbm.mymangas.services.CollectionServiceComponent
import com.gbm.mymangas.services.impl.CollectionServiceComponentImpl
import com.gbm.mymangas.utils.messages.{ Error, Succeed }
import reactivemongo.api.commands.{ DefaultWriteResult, LastError, WriteConcernError, WriteError }

import scala.concurrent.Future

/**
 * Created by gbmetzner on 12/22/15.
 */
class CollectionServiceSpec extends UnitSpec {

  val dragonBall = Collection(publisher = "Panini", name = "Dragon Ball", searchParam = "dragon ball ed. #")
  val error = WriteConcernError(1, "error")
  val actionOk = LastError(true, None, None, None, 1, None, false, None, None, false, None, None)
  val actionNOk = DefaultWriteResult(false, 1, Seq.empty[WriteError], Some(error), None, None)
  val fWriteResultInsertOk = (c: Collection) => Future.successful(actionOk)
  val fWriteResultInsertNOk = (p: Collection) => Future.successful(actionNOk)
  val fWriteResultUpdateOk = (id: UUID, p: Collection) => Future.successful(actionOk)
  val fWriteResultUpdateNOk = (id: UUID, p: Collection) => Future.successful(actionNOk)
  val fWriteResultRemoveOk = (id: UUID) => Future.successful(actionOk)
  val fWriteResultRemoveNOk = (id: UUID) => Future.successful(actionNOk)
  val findNone = (p: Predicate) => Future.successful(None)
  val findOne = (p: Predicate) => Future.successful(Some(dragonBall))
  val findNoneList = (p: Predicate) => Future.successful(Nil)
  val findOneList = (p: Predicate) => Future.successful(List(dragonBall))
  val updateEntireColl = (collection: String, update: Boolean) => Future.successful(())

  "A CollectionService" should "insert a collection correctly" in {
    val collectionServiceComponent = new CollectionServiceComponent with CollectionServiceComponentImpl

    val result = collectionServiceComponent.collectionService.insert(dragonBall)(fWriteResultInsertOk)(findNone)

    result.futureValue shouldBe Right(Succeed("collection.added"))
  }

  it should "not insert a collection correctly due an error" in {
    val collectionServiceComponent = new CollectionServiceComponent with CollectionServiceComponentImpl

    val result = collectionServiceComponent.collectionService.insert(dragonBall)(fWriteResultInsertNOk)(findNone)

    result.futureValue shouldBe Left(Error("error.general"))
  }

  it should "not insert a collection correctly due an existent collection in db" in {
    val collectionServiceComponent = new CollectionServiceComponent with CollectionServiceComponentImpl

    val result = collectionServiceComponent.collectionService.insert(dragonBall)(fWriteResultInsertOk)(findOne)

    result.futureValue shouldBe Left(Error("collection.already.exists"))
  }

  it should "update a collection correctly" in {
    val collectionServiceComponent = new CollectionServiceComponent with CollectionServiceComponentImpl

    val result = collectionServiceComponent.collectionService.update(dragonBall.id, dragonBall)(fWriteResultUpdateOk)(findOneList)(updateEntireColl)

    result.futureValue shouldBe Right(Succeed("collection.updated"))
  }

  it should "not update a collection correctly due an error" in {
    val collectionServiceComponent = new CollectionServiceComponent with CollectionServiceComponentImpl

    val result = collectionServiceComponent.collectionService.update(dragonBall.id, dragonBall)(fWriteResultUpdateNOk)(findOneList)(updateEntireColl)

    result.futureValue shouldBe Left(Error("error.general"))
  }

  it should "not update a collection correctly due a collection not found" in {
    val collectionServiceComponent = new CollectionServiceComponent with CollectionServiceComponentImpl

    val result = collectionServiceComponent.collectionService.update(dragonBall.id, dragonBall)(fWriteResultUpdateOk)(findNoneList)(updateEntireColl)

    result.futureValue shouldBe Left(Error("collection.not.found"))
  }

  it should "remove a collection correctly" in {
    val collectionServiceComponent = new CollectionServiceComponent with CollectionServiceComponentImpl

    val result = collectionServiceComponent.collectionService.remove(dragonBall.id)(fWriteResultRemoveOk)(findOne)

    result.futureValue shouldBe Right(Succeed("collection.removed"))
  }

  it should "not remove a collection correctly due an error" in {
    val collectionServiceComponent = new CollectionServiceComponent with CollectionServiceComponentImpl

    val result = collectionServiceComponent.collectionService.remove(dragonBall.id)(fWriteResultRemoveNOk)(findOne)

    result.futureValue shouldBe Left(Error("error.general"))
  }

  it should "not remove a collection correctly due a collection not found" in {
    val collectionServiceComponent = new CollectionServiceComponent with CollectionServiceComponentImpl

    val result = collectionServiceComponent.collectionService.remove(dragonBall.id)(fWriteResultRemoveOk)(findNone)

    result.futureValue shouldBe Left(Error("collection.not.found"))
  }
}
