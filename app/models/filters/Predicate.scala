package models.filters

import play.api.libs.json.JsObject
import reactivemongo.api.QueryOpts

/**
 * @author Gustavo Metzner on 10/12/15.
 */
trait Predicate {

  val limit: Option[Int]
  val skip: Option[Int]

  def filter: JsObject

  def sort: JsObject

  def queryOpts: QueryOpts = QueryOpts().batchSize(limit getOrElse 0).skip(skip getOrElse 0)

}
