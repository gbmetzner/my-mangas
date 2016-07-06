package com.gbm.mymangas.controllers

import play.api.mvc._

class Application extends Controller {

  def main(any: String): Action[AnyContent] = Action {
    Ok(views.html.main())
  }

}
