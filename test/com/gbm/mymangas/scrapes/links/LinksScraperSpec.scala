package com.gbm.mymangas.scrapes.links

import com.gbm.mymangas.base.UnitSpec
import com.gbm.mymangas.browser.FakeBrowser

/**
  * Created by gbmetzner on 11/15/15.
  */
class LinksScraperSpec extends UnitSpec {

  "A PaniniLinksScraper" should "extract one link from a document" in {

    val document = getDocument("berserk_links")

    val links = PaniniLinksScraper(FakeBrowser).scrape(document)

    links.size shouldBe 1
  }

  "A JBCLinksScraper" should "extract 10 links from a document" in {

    val document = getDocument("video_girl_links")

    val links = JBCLinksScraper(FakeBrowser).scrape(document)

    links.size shouldBe 10
  }

}
