package fr.warzou.skyblock.nms.versioning.api.utils.io

import fr.warzou.skyblock.nms.versioning.api.utils.io.Nibble._

case class Nibble(byte: Byte) {

  def this(int: Int) = this(int.toByte)

  def ++(nibble: Nibble): Byte = ((byte << 4) + nibble.byte).toByte

  def +(nibble: Nibble): Nibble = (byte + nibble.byte).toNibble

  def toByte: Byte = byte

  def toShort: Short = byte.toShort

  def toInt: Int = byte.toInt

  def toLong : Long = byte.toLong

  override def toString: String = s"Nibble{byte=$byte}"
}

case object Nibble {

  val zero: Nibble = Nibble(0)

  def nibbleLeft(byte: Byte): Nibble = ((byte & 0XF0) >> 4).toNibble
  def nibbleRight(byte: Byte): Nibble = (byte & 0x0F).toNibble

  def toByteArray(nibbles: Array[Nibble]): Array[Byte] = (0 until nibbles.length / 2)
      .map(i => nibbles(i * 2) ++ (if (nibbles.length <= i * 2 + 1) nibbles(i * 2 + 1) else Nibble(0))).toArray

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

    def toNibbleArray: Array[Nibble] =
      Array(((short & 0xFF00) >> 8).toByte.toNibbleArray, (short & 0x00FF).toByte.toNibbleArray).flatten
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