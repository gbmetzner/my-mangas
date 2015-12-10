package com.gbm.mymangas.utils.files.download

import java.io.File
import java.net.URL

import scala.sys.process._

/**
  * Created by gbmetzner on 12/5/15.
  */
trait FileDownloaderComponent {

  def fileDownloader: FileDownloader

  trait FileDownloader {
    def downloadImage(url: String, file: String): String

    def extractExtension(url: String): String

    object default {
      def download(url: String, filename: String): String = {
        new URL(url) #> new File(filename) !!
      }

      def extractExtension(url: String): String = {
        val lastIndexOfDot = url.lastIndexOf(".")
        val extension = url.substring(lastIndexOfDot)
        if (extension.length > 4) ".jpg" else extension
      }
    }

  }


}
