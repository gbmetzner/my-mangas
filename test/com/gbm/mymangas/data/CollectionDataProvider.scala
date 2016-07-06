package com.gbm.mymangas.data

import com.gbm.mymangas.models.Collection

/**
 * Created by gbmetzner on 12/9/15.
 */
object CollectionDataProvider extends TestData[Collection] {

  add("berserk_1" -> Collection(publisher = "Panini", name = "Berserk", searchParam = "Berserk - Ed. #"))

  add("video_girl_1" -> Collection(publisher = "JBC", name = "Video Girl", searchParam = "video-girl"))

}
