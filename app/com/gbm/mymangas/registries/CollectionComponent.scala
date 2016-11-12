package com.gbm.mymangas.registries

import javax.inject.Inject

import com.gbm.mymangas.repositories.CollectionRepository
import com.gbm.mymangas.services.CollectionService

/**
  * @author Gustavo Metzner on 12/8/15.
  */
class CollectionComponent @Inject()(val collectionService: CollectionService,
                                    val collectionRepository: CollectionRepository)