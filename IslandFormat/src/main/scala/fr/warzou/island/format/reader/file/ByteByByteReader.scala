package fr.warzou.island.format.reader.file

import java.io.{File, FileInputStream}

class ByteByByteReader(file: File) {

  val bytes: Array[Byte] = new Array(file.length().toInt)
  read()

  private def read(): Unit = {
    val outputStream: FileInputStream = new FileInputStream(this.file)
    outputStream.read(this.bytes)
    outputStream.close()
  }

}
