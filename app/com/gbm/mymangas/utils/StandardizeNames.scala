package com.gbm.mymangas.utils

import java.text.Normalizer

/**
  * @author Gustavo Metzner on 10/20/15.
  */
object StandardizeNames {

  implicit class StandardizeName(name: String) {
    def standardize: String = {
      Normalizer.normalize(name, Normalizer.Form.NFD)
        .replaceAll("[^\\p{ASCII}]", "")
        .replaceAll(" ", "_")
        .replaceAll("#", "_")
        .toLowerCase
    }
  }

}
