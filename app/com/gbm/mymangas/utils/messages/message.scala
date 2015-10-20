package com.gbm.mymangas.utils.messages

/**
 * @author Gustavo Metzner on 10/11/15.
 */
sealed trait Result {
  val message: String
}

sealed trait Failed extends Result

final case class Succeed(message: String) extends Result

final case class Warning(message: String) extends Failed

final case class Error(message: String) extends Failed
