package com.gbm.mymangas.registries

import javax.inject.Inject

import com.gbm.mymangas.repositories.MangaRepository
import com.gbm.mymangas.services.impl.MangaService

/**
  * @author Gustavo Metzner on 12/8/15.
  */
class MangaComponent @Inject()(val mangaService: MangaService,
                               val mangaRepository: MangaRepository)
