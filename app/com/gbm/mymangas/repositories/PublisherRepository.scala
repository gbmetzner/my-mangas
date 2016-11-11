package com.gbm.mymangas.repositories

import com.gbm.mymangas.models.Publisher
import com.gbm.mymangas.repositories.impl.PublisherRepositoryImpl
import com.google.inject.ImplementedBy

/**
  * Created by gbmetzner on 12/8/15.
  */
@ImplementedBy(classOf[PublisherRepositoryImpl])
trait PublisherRepository extends Repository[Publisher]
