package com.gbm.mymangas.scrapes.links

import com.gbm.mymangas.base.UnitSpec

/**
  * Created by gbmetzner on 11/15/15.
  */
class LinksScraperSpec extends UnitSpec {

  "A PaniniLinksScraper" should "extract one link from a document" in {

    val document = getDocument("berserk_links")

    val links = PaniniLinksScraper.scrape(document)

    links.size shouldBe 1
    links.head shouldBe "http://www.paninicomics.com.br/web/guest/productDetail?viewItem=765014"
  }

  "A JBCLinksScraper" should "extract 10 links from a document" in {

    val document = getDocument("video_girl_links")

    val links = JBCLinksScraper.scrape(document)

    links.size shouldBe 10
    links.head shouldBe "http://mangasjbc.uol.com.br/video-girl-len-30/"
    links.last shouldBe "http://mangasjbc.uol.com.br/video-girl-ai-21/"
  }

}
