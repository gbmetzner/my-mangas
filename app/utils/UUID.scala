package utils

/** This package provides methods to deal with UUID */
object UUID {

  /** Generates a new UUID */
  def generate(): java.util.UUID = java.util.UUID.randomUUID()
}
