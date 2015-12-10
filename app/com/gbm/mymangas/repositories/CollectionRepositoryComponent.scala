package com.gbm.mymangas.repositories

import java.util.UUID
import javax.inject.Inject

import com.gbm.mymangas.models.filters.Predicate
import com.gbm.mymangas.models.{Collection, Page}
import com.gbm.mymangas.utils.json.CollectionParser.collectionFormatterRepo
import play.api.libs.json.Json
import play.modules.reactivemongo.{ReactiveMongoModule, DefaultReactiveMongoApi, ReactiveMongoApi}
import play.modules.reactivemongo.json._
import reactivemongo.api.commands.WriteResult

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * Created by gbmetzner on 12/5/15.
  */
trait CollectionRepositoryComponent {

  def collectionRepository: CollectionRepository

  trait CollectionRepository extends Repository[Collection]

}