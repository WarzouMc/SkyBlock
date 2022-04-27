package fr.warzou.island.format.writer.file

import fr.il_totore.spigotmetadata.api.SpigotMetadataAPI
import fr.warzou.island.format.core.RawIsland
import fr.warzou.island.format.core.common.Version
import org.bukkit.block.Block

import java.io.{File, FileOutputStream}

class Writer(file: File) {

  private val outputStream: FileOutputStream = new FileOutputStream(file)
  private val nbtManager = SpigotMetadataAPI.getAPI.getNBTManager

  def write(island: RawIsland): Unit = {
    write("skbl")
    writeVersion(island.minecraftVersion)
    writeBlocks(island.blocks)
  }

  private def writeVersion(version: Version): Unit = {
    writeU1Int(version.major)
    writeU1Int(version.minor)
    writeU1Int(version.revision)
  }

  private def writeBlocks(blocks: List[Block]): Unit = {
    blocks.foreach(block => nbtManager.getNBTTag(block)//todo)
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