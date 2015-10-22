package com.gbm.mymangas.utils

/**
 * @author Gustavo Metzner on 10/20/15.
 */
object StandardizeNames {

  implicit class StandardizeName(name: String) {
    def standardize: String = name.replaceAll(" ", "_").toLowerCase
  }

}
