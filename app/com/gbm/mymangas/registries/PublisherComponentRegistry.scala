package com.gbm.mymangas.registries

import com.gbm.mymangas.repositories.impl.PublisherRepositoryComponentImpl
import com.gbm.mymangas.services.impl.PublisherServiceComponentImpl

/**
  * Created by gbmetzner on 12/8/15.
  */
trait PublisherComponentRegistry
  extends PublisherServiceComponentImpl
  with PublisherRepositoryComponentImpl
