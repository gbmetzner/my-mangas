package com.gbm.mymangas.upload

import java.io.File

import com.gbm.mymangas.utils.files.upload.FileUploader

/**
  * Created by gbmetzner on 12/5/15.
  */
object FakeFileUploader extends FileUploader {
  override def upload(externalPath: String, file: File): String = "done"
}
