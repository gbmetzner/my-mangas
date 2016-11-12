package com.gbm.mymangas.utils

import java.io.File

import com.sksamuel.scrimage.{Image => SImage}

/**
  * Utility class for image processing.
  *
  * @author Gustavo Metzner on 12/5/15.
  */
object Image {

  /**
    * Resize an image.
    *
    * @param file   The image to be resized.
    * @param width  The new width.
    * @param height The new height.
    * @return The new image resized.
    */
  def resize(file: File)(width: Int, height: Int): File = {
    SImage.fromFile(file).cover(width, height).output(file)
  }

}
