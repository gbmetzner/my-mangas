package com.gbm.mymangas.scrapes.mangas

import com.gbm.mymangas.base.UnitSpec

/**
  * Created by gbmetzner on 11/16/15.
  */
class MangaScraperSpec extends UnitSpec {

  "A PaniniMangaScraper" should "extract a (Manga, Links) from a document" in {
    val document = getDocument("berserk_1_manga")

    val (manga, url) = PaniniMangaScraper.scrape(document)

    manga.collection shouldBe "Berserk"
    manga.name shouldBe "Berserk"
    manga.number shouldBe 1
    url shouldBe "http://www.paninicomics.com.br/image/image_gallery?img_id=7295645&t=1410887306210"
  }

  "A JBCMangaScraper" should "extract a (Manga, Links) from a document" in {
    val document = getDocument("video_girl_1_manga")

    val (manga, url) = JBCMangaScraper.scrape(document)

    manga.collection shouldBe "Video Girl Ai"
    manga.name shouldBe "Apaixone-se também por Video Girl Ai!"
    manga.number shouldBe 1
    url shouldBe "http://jbchost.com.br/mangasjbc/wp-content/uploads/2005/05/capa_video_girl_ai_01_g-200x311.jpg"
  }

}
