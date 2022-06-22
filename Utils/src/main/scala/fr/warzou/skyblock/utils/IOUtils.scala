package fr.warzou.skyblock.utils

import java.io.{FileReader, OutputStream}
import scala.annotation.tailrec

object IOUtils {

  /**
   * Write a variable length int
   * <p>More detail on <a href="https://wiki.vg/Protocol#VarInt_and_VarLong">wiki.vg</a></p>
   * @param outputStream where value will be written
   * @param value value to write
   * @param writer a function who write a byte
   * @tparam A an Output Stream
   */
  @tailrec
  def writeVarInt[A <: OutputStream](outputStream: A, value: Int, writer: Byte => Unit): Unit = {
    if ((value & ~0x7F) == 0) writer(value.toByte)
    else {
      writer(((value & 0x7F) | 0x80).toByte)
      writeVarInt(outputStream, value >>> 7, writer)
    }
  }

  /**
   * Read a variable length int
   * <p>More detail on <a href="https://wiki.vg/Protocol#VarInt_and_VarLong">wiki.vg</a></p>
   * @param reader where value will be read
   * @return read value
   */
  def readVarInt(reader: FileReader): Int = readVarInt(reader, 0, 0)

  @tailrec
  private def readVarInt(reader: FileReader, value: Int, position: Int): Int = {
    val currentByte = reader.read.toByte
    val newValue = value | ((currentByte & 0x7f) << position)
    if ((currentByte & 0x80) == 0 && position + 7 < 32) newValue
    else if (position + 7 >= 32) throw new RuntimeException("VarInt is too big")
    else readVarInt(reader, newValue, position + 7)
  }
}
