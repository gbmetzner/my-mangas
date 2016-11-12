package com.gbm.mymangas.registries

import javax.inject.Inject

import com.gbm.mymangas.repositories.UserRepository
import com.gbm.mymangas.services.UserService

/**
 * @author Gustavo Metzner on 12/8/15.
 */
class UserComponent @Inject() (
  val userService: UserService,
  val userRepository: UserRepository
)