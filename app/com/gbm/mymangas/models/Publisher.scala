package com.gbm.mymangas.models

import java.util.UUID

import org.joda.time.DateTime
import com.gbm.mymangas.utils.UUID._

/**
 * A publisher who publish the volumes.
 *
 * @constructor create a new person with a name and age.
 * @param id the publisher's id
 * @param name the publisher's name
 * @param createdAt the time when the publisher was created
 * @param updatedAt the time when the publisher was updated
 *
 * @author Gustavo Metzner
 */
case class Publisher(
  id: UUID = generate(),
    name: String,
    createdAt: DateTime = DateTime.now(),
    updatedAt: DateTime = DateTime.now()
) {

  def imagePath: String = s"${id.toString}.jpg"
}
