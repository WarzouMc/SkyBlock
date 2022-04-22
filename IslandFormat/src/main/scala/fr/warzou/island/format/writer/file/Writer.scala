package fr.warzou.island.format.writer.file

import fr.warzou.island.format.core.RawIsland

import java.io.{Closeable, File, FileOutputStream, FileWriter}

class Writer(file: File) {

  private val outputStream: FileOutputStream = new FileOutputStream(file)

  def write(island: RawIsland): Unit = {
    write("skbl")
    writeU1Int(-1)
    writeU1Int(15)
    writeU1Int(5)
  }

  private def write(string: String): Unit = write(string.getBytes)

  private def writeU1Int(int: Int): Unit = {
    outputStream.write(int)
  }

  private def write(byte: Byte): Unit = outputStream.write(byte)

  private def write(bytes: Array[Byte]): Unit = outputStream.write(bytes)

  def flush(): Unit = this.outputStream.flush()
  def close(): Unit = this.outputStream.close()
}