package com.gbm.mymangas.registries

import com.gbm.mymangas.repositories.impl.UserRepositoryComponentImpl
import com.gbm.mymangas.services.impl.UserServiceComponentImpl

/**
 * Created by gbmetzner on 12/8/15.
 */
trait UserComponentRegistry
  extends UserServiceComponentImpl
  with UserRepositoryComponentImpl
