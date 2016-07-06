package com.gbm.mymangas.utils.files.upload

import java.io.File

/**
 * Created by gbmetzner on 12/5/15.
 */
trait FileUploaderComponentImpl extends FileUploaderComponent {

  override def fileUploader: FileUploader = new FileUploaderImpl

  class FileUploaderImpl extends FileUploader {

    import SmartFile._

    override def upload(externalPath: String, file: File): String = {
      SmartFile.upload(externalPath, file)(makeDirectory)(resize)(uploadFile)(makePublicLink)(parsePublicLink)
    }
  }

}
