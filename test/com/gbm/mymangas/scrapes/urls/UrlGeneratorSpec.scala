package com.gbm.mymangas.scrapes.urls

import com.gbm.mymangas.base.UnitSpec
import com.gbm.mymangas.browser.FakeBrowser

/**
  * Created by gbmetzner on 11/18/15.
  */
class UrlGeneratorSpec extends UnitSpec {
  "A PaniniUrlGenerator" should "returns a 2 documents from a urls" in {
    val result = PaniniUrlGenerator(FakeBrowser).generate("berserk_#_manga", 0)
    result.size shouldBe 2
  }
  it should "returns 1 document from the urls" in {
    val result = PaniniUrlGenerator(FakeBrowser).generate("berserk_#_manga", 1)
    result.size shouldBe 1
  }
  it should "returns no document from the urls" in {
    val result = PaniniUrlGenerator(FakeBrowser).generate("berserk_#_manga", 2)
    result.size shouldBe 0
  }

  "A JBCUrlGenerator" should "returns a 2 documents from a urls" in {
    val result = JBCUrlGenerator(FakeBrowser).generate("fairy-tail", 0)
    result.size shouldBe 2
  }
  it should "returns 1 document from the urls" in {
    val result = JBCUrlGenerator(FakeBrowser).generate("fairy-tail", 40)
    result.size shouldBe 1
  }
  it should "returns no document from the urls" in {
    val result = JBCUrlGenerator(FakeBrowser).generate("fairy-tail", 50)
    result.size shouldBe 0
  }
}
