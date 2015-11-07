package com.gbm.mymangas.models

import java.util.UUID

import com.gbm.mymangas.utils.UUID._
import org.joda.time.DateTime

/**
  * Created by gbmetzner on 11/5/15.
  */
case class User(id: UUID = generate(),
                name: String,
                username: String,
                password: String,
                createdAt: DateTime = DateTime.now(),
                updatedAt: DateTime = DateTime.now())