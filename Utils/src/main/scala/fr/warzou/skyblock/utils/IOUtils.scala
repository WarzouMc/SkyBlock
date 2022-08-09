package fr.warzou.skyblock.utils

import java.io.{FileInputStream, FileReader, OutputStream}
import scala.annotation.tailrec

case object IOUtils {

  /**
   * Write a variable length int
   * <p>More detail on <a href="https://wiki.vg/Protocol#VarInt_and_VarLong">wiki.vg</a></p>
   * @param outputStream where value will be written
   * @param value value to write
   */
  @tailrec
  def writeVarInt(outputStream: OutputStream, value: Int): Unit = {
    if ((value & ~0x7F) == 0) outputStream.write(value)
    else {
      outputStream.write(((value & 0x7F) | 0x80).toByte)
      writeVarInt(outputStream, value >>> 7)
    }
  }

  /**
   * Read a variable length int
   * <p>More detail on <a href="https://wiki.vg/Protocol#VarInt_and_VarLong">wiki.vg</a></p>
   * @param reader where value will be read
   * @return read value
   */
  def readVarInt(reader: FileInputStream): Int = readVarInt(reader, 0, 0)

  @tailrec
  private def readVarInt(reader: FileInputStream, value: Int, position: Int): Int = {
    val currentByte = reader.read.toByte
    val newValue = value | ((currentByte & 0x7f) << position)
    if ((currentByte & 0x80) == 0) newValue
    else if (position + 7 >= 32) throw new RuntimeException("VarInt is too big")
    else readVarInt(reader, newValue, position + 7)
  }
}
