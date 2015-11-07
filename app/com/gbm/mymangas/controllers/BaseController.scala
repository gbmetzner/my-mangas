package com.gbm.mymangas.controllers

import com.gbm.mymangas.controllers.security.Security
import com.typesafe.scalalogging.LazyLogging
import play.api.mvc.Controller

/**
  * @author Gustavo Metzner on 10/10/15.
  */
trait BaseController
  extends Controller with Security with LazyLogging

