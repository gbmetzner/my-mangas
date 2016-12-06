package com.gbm.mymangas.controllers

import play.api.Logger
import play.api.mvc._

class Application extends Controller {

  /**
   * It
   * @param any
   * @return
   */
  def main(any: String): Action[AnyContent] = Action {
    Logger debug s"First call to $any..."
    Ok(views.html.main())
  }

}
