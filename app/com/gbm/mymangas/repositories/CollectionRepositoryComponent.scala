package com.gbm.mymangas.repositories

import com.gbm.mymangas.models.Collection

/**
 * Created by gbmetzner on 12/5/15.
 */
trait CollectionRepositoryComponent {

  def collectionRepository: CollectionRepository

  trait CollectionRepository extends Repository[Collection]

}
