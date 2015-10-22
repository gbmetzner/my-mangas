package com.gbm.mymangas.models

import java.util.UUID

import com.gbm.mymangas.utils.UUID._
import org.joda.time.DateTime

/**
 * @author Gustavo Metzner on 10/13/15.
 */
case class Collection(id: UUID = generate(),
                      publisher: String,
                      name: String,
                      searchParam: String,
                      isComplete: Boolean = false,
                      createdAt: DateTime = DateTime.now(),
                      updatedAt: DateTime = DateTime.now())
