package fr.warzou.skyblock.utils.collection.map

class AlreadyPresentException(message: String, throwable: Throwable) extends RuntimeException(message, throwable) {
  def this() = this(null, null)

  def this(throwable: Throwable) = this(null, throwable)

  def this(message: String) = this(message, null)
}
