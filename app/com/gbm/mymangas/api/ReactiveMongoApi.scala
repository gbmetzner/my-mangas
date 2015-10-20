package com.gbm.mymangas.api

import reactivemongo.api.{DB, MongoConnection, MongoDriver}

/**
 * @author Gustavo Metzner
 */
trait ReactiveMongoApi {
  def driver: MongoDriver

  def connection: MongoConnection

  def db: DB
}
