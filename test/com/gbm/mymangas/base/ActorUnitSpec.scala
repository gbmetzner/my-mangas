package com.gbm.mymangas.base

import akka.actor.ActorSystem
import akka.testkit.{ TestKit, ImplicitSender }
import com.typesafe.config.ConfigFactory
import org.scalactic.ConversionCheckedTripleEquals
import org.scalatest.{ FlatSpecLike, BeforeAndAfterAll, Matchers }

/**
 * Created by gbmetzner on 12/4/15.
 */
abstract class ActorUnitSpec(actorSystem: String)
    extends TestKit(ActorSystem(actorSystem))
    with FlatSpecLike
    with BeforeAndAfterAll
    with Matchers
    with ConversionCheckedTripleEquals
    with ImplicitSender {

  override def afterAll(): Unit = {
    system.terminate()
  }

}