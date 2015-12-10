package com.gbm.mymangas.upload

import java.io.File

import com.gbm.mymangas.utils.files.upload.FileUploaderComponent

/**
  * Created by gbmetzner on 12/5/15.
  */
trait FakeFileUploaderComponentImpl extends FileUploaderComponent {
  override def fileUploader: FileUploader = new FakeUploaderImpl

  class FakeUploaderImpl extends FileUploader {
    override def upload(externalPath: String, file: File): String = "done"
  }

}
