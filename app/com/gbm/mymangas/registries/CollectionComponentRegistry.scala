package com.gbm.mymangas.registries

import com.gbm.mymangas.repositories.impl.CollectionRepositoryComponentImpl
import com.gbm.mymangas.services.impl.CollectionServiceComponentImpl

/**
 * Created by gbmetzner on 12/8/15.
 */
trait CollectionComponentRegistry
  extends CollectionServiceComponentImpl
  with CollectionRepositoryComponentImpl
