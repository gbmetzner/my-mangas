package com.gbm.mymangas.utils.files.upload

import java.io.File

/**
  * Created by gbmetzner on 12/5/15.
  */
object SmartFileUploader extends FileUploader {

  import smartfile._

  override def upload(externalPath: String, file: File): String = {
    smartfile.upload(externalPath, file)(makeDirectory)(resize)(uploadFile)(makePublicLink)(parsePublicLink)
  }

}
