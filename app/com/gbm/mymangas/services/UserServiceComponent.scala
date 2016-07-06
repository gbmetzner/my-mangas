package com.gbm.mymangas.services

import com.gbm.mymangas.models.User

/**
 * Created by gbmetzner on 11/5/15.
 */
trait UserServiceComponent {

  def userService: UserService

  trait UserService extends Service[User]

}
