package com.gbm.mymangas.registries

import com.gbm.mymangas.repositories.impl.MangaRepositoryComponentImpl
import com.gbm.mymangas.services.impl.MangaServiceComponentImpl
import com.gbm.mymangas.utils.files.upload.FileUploaderComponentImpl

/**
 * Created by gbmetzner on 12/8/15.
 */
trait MangaComponentRegistry
  extends MangaServiceComponentImpl
  with MangaRepositoryComponentImpl
  with FileUploaderComponentImpl
