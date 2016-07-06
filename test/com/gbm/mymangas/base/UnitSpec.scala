package com.gbm.mymangas.base

import com.gbm.mymangas.browser.FakeBrowser
import net.ruippeixotog.scalascraper.model.Document
import org.scalatest._
import org.scalatest.concurrent.ScalaFutures

/**
 * Created by gbmetzner on 11/15/15.
 */

abstract class UnitSpec
    extends FlatSpec
    with Matchers
    with OptionValues
    with Inside
    with Inspectors
    with ScalaFutures {

  def getDocument(key: String): Document = FakeBrowser.get(key) match {
    case Some(doc) => doc
    case None => throw new IllegalArgumentException(s"Key $key not found.")
  }

}
