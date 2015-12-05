package com.gbm.mymangas.utils.files.download

/**
  * Created by gbmetzner on 12/5/15.
  */
object DefaultFileDownloader extends FileDownloader {
  override def downloadImage(url: String, file: String): String = {
    default.download(url, file)
  }

  override def extractExtension(url: String): String = {
    default.extractExtension(url)
  }
}
