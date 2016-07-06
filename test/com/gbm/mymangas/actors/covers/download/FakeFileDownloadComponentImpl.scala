package com.gbm.mymangas.actors.covers.download

import java.io.FileNotFoundException

import com.gbm.mymangas.utils.files.download.FileDownloaderComponent

/**
 * Created by gbmetzner on 12/5/15.
 */
trait FakeFileDownloadComponentImplError extends FileDownloaderComponent {
  override def fileDownloader: FileDownloader = new FakeFileDownloadImpl

  class FakeFileDownloadImpl extends FileDownloader {
    override def downloadImage(url: String, file: String): String = throw new FileNotFoundException("Download Error")

    override def extractExtension(url: String): String = ".jpg"
  }

}

trait FakeFileDownloadComponentImpl extends FileDownloaderComponent {
  override def fileDownloader: FileDownloader = new FakeFileDownloadImpl

  class FakeFileDownloadImpl extends FileDownloader {
    override def downloadImage(url: String, file: String): String = "/tmp/berserk_1.jpg"

    override def extractExtension(url: String): String = ".jpg"
  }

}