package com.gbm.mymangas.registries

import javax.inject.Inject

import com.gbm.mymangas.repositories.PublisherRepository
import com.gbm.mymangas.services.PublisherService

/**
  * @author Gustavo Metzner on 12/8/15.
  */
class PublisherComponent @Inject()(val publisherService: PublisherService,
                                   val publisherRepository: PublisherRepository)

