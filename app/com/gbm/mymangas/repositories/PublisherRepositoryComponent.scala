package com.gbm.mymangas.repositories

import com.gbm.mymangas.models.Publisher

/**
  * Created by gbmetzner on 12/8/15.
  */
trait PublisherRepositoryComponent {

  def publisherRepository: PublisherRepository

  trait PublisherRepository extends Repository[Publisher]

}
