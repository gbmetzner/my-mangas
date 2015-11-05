package com.gbm.mymangas.models

import java.util.UUID

import com.gbm.mymangas.utils.Config
import com.gbm.mymangas.utils.UUID._
import org.joda.time.DateTime

/**
 * @author Gustavo Metzner on 10/13/15.
 */
case class Manga(id: UUID = generate(),
                 collection: String,
                 name: String,
                 number: Int,
                 doIHaveIt: Boolean = false,
                 publicLink: String = Config.defaultCover,
                 createdAt: DateTime = DateTime.now(),
                 updatedAt: DateTime = DateTime.now()) {

  def fullName: String = s"$collection - #$number"
}