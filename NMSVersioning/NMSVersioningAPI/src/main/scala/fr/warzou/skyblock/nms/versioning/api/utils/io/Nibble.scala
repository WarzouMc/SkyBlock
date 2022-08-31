package fr.warzou.skyblock.nms.versioning.api.utils.io

case class Nibble(byte: Byte) {

  def this(int: Int) = this(int.toByte)

  def ++(nibble: Nibble): Byte = ((byte << 4) + nibble.byte).toByte

  def toByte: Byte = byte

  def toShort: Short = byte.toShort

  def toInt: Int = byte.toInt

  def toLong : Long = byte.toLong

  override def toString: String = s"Nibble{byte=$byte}"
}

case object Nibble {

  def nibbleLeft(byte: Byte): Nibble = ((byte & 0XF0) >> 4).toNibble
  def nibbleRight(byte: Byte): Nibble = (byte & 0x0F).toNibble

  def toNibbleArray(bytes: Array[Byte]): Array[Nibble] =
    bytes.flatMap(byte => List(byte.nibbleLeft, byte.nibbleRight))

  implicit class ImplicitByte(byte: Byte) {
    def toNibble: Nibble = Nibble((byte & 0x0F).toByte)

    def nibbleLeft: Nibble = Nibble.nibbleLeft(byte)

    def nibbleRight: Nibble = toNibble

    def toNibbleArray: Array[Nibble] = Array(nibbleLeft, nibbleRight)
  }

  implicit class ImplicitShort(short: Short) {
    def toNibble: Nibble = Nibble((short & 0x0F).toByte)

    def toNibbleArray: Array[Nibble] = Array((short & 0xF000) >> 12, (short & 0x0F00) >> 8, (short & 0x00F0) >> 4,
      short & 0x000F).map(new Nibble(_))
  }

  implicit class ImplicitInt(int: Int) {
    def toNibble: Nibble = Nibble((int & 0x0F).toByte)

    def toNibbleArray: Array[Nibble] =
      Array(((int & 0xFFFF0000) >> 16).toShort.toNibbleArray, (int & 0x0000FFFF).toShort.toNibbleArray).flatten
  }

  implicit class ImplicitLong(long: Long) {
    def toNibble: Nibble = Nibble((long & 0x0F).toByte)

    def toNibbleArray: Array[Nibble] =
      Array(((long & 0xFFFFFFFF00000000L) >> 32).toInt.toNibbleArray, (long & 0x00000000FFFFFFFF).toInt.toNibbleArray).flatten
  }
}