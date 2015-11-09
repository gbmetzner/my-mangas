package com.gbm.mymangas.services

import com.typesafe.scalalogging.LazyLogging
import play.modules.reactivemongo.ReactiveMongoApi
import play.modules.reactivemongo.json.collection.JSONCollection

/**
  * @author Gustavo Metzner on 10/16/15.
  */
trait Service extends LazyLogging {

  protected[services] val collectionName: String

  protected[services] val reactiveMongoApi: ReactiveMongoApi

  /** */
  protected[services] def collection: JSONCollection = reactiveMongoApi.db.collection[JSONCollection](collectionName)

}
