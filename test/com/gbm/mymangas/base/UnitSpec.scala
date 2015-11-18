package com.gbm.mymangas.base

import com.gbm.mymangas.browser.FakeBrowser
import org.jsoup.nodes.Document
import org.scalatest._

/**
  * Created by gbmetzner on 11/15/15.
  */

abstract class UnitSpec
  extends FlatSpec
  with Matchers
  with OptionValues
  with Inside
  with Inspectors {


  def getDocument(key: String): Document = FakeBrowser.get(key) match {
    case Some(doc) => doc
    case None => throw new IllegalArgumentException(s"Key $key not found.")
  }

}
