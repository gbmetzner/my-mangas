package com.gbm.mymangas.data

import com.gbm.mymangas.models.Manga

/**
 * Created by gbmetzner on 12/4/15.
 */
object MangaDataProvider extends TestData[Manga] {

  val berserk1 = "berserk1" -> Manga(collection = "Berserk", number = 1)

  add(berserk1)

}
