package models.filters

import play.api.libs.json.JsObject

/**
 * @author Gustavo Metzner on 10/12/15.
 */
trait Predicate {
  def filter: JsObject

  def sort: JsObject
}
