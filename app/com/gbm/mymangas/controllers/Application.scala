package com.gbm.mymangas.controllers

import com.typesafe.scalalogging.LazyLogging
import play.api.mvc._

class Application extends Controller with LazyLogging {

  /**
    * It
    * @param any
    * @return
    */
  def main(any: String): Action[AnyContent] = Action {
    logger info ""
    Ok(views.html.main())
  }

}
