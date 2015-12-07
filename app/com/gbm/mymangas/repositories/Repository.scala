package com.gbm.mymangas.repositories

import com.typesafe.scalalogging.LazyLogging
import play.modules.reactivemongo.ReactiveMongoApi
import play.modules.reactivemongo.json.collection.JSONCollection

/**
  * Created by gbmetzner on 12/5/15.
  */
trait Repository extends LazyLogging {

  protected[repositories] val collectionName: String

  protected[repositories] val reactiveMongoApi: ReactiveMongoApi

  protected[repositories] def collection: JSONCollection = reactiveMongoApi.db.collection[JSONCollection](collectionName)
}
