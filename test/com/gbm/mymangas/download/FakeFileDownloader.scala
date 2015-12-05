package com.gbm.mymangas.download

import com.gbm.mymangas.utils.files.download.FileDownloader

/**
  * Created by gbmetzner on 12/5/15.
  */
object FakeFileDownloader extends FileDownloader {

  override def downloadImage(url: String, file: String): String = "done"

  override def extractExtension(url: String): String = ".jpg"

}
