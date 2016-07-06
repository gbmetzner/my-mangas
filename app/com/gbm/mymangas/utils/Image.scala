package com.gbm.mymangas.utils

import java.io.File

import com.sksamuel.scrimage.{ Image => SImage }

/**
 * Created by gbmetzner on 12/5/15.
 */
object Image {

  def resize(file: File)(width: Int, height: Int) {
    SImage.fromFile(file).cover(width, height).output(file)
  }

}
