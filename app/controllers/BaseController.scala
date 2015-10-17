package controllers

import com.typesafe.scalalogging.LazyLogging
import play.api.mvc.Controller
import play.modules.reactivemongo.json.collection.JSONCollection
import play.modules.reactivemongo.{MongoController, ReactiveMongoComponents}

/**
 * @author Gustavo Metzner on 10/10/15.
 */
trait BaseController
  extends Controller
  with LazyLogging {

  protected[controllers] val collectionName: String

  /** */
  protected[controllers] def collection: JSONCollection = ???
    //db.collection[JSONCollection](collectionName)

}
