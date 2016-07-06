package com.gbm.mymangas.repositories

import com.gbm.mymangas.models.Manga

/**
 * Created by gbmetzner on 12/6/15.
 */
trait MangaRepositoryComponent {

  def mangaRepository: MangaRepository

  trait MangaRepository extends Repository[Manga]

}
