package fr.warzou.island.format.writer.file

import fr.il_totore.spigotmetadata.api.SpigotMetadataAPI
import fr.warzou.island.format.core.RawIsland
import fr.warzou.island.format.core.common.Version
import fr.warzou.island.format.core.common.block.NotLocateBlock
import fr.warzou.island.format.core.common.block.tileentities.BlockEntity
import org.apache.commons.lang.ClassUtils
import org.bukkit.Location
import org.bukkit.block.Block
import org.bukkit.plugin.Plugin

import java.io.{File, FileOutputStream}
import java.lang.reflect.{Field, Modifier}
import scala.annotation.tailrec
import scala.math.BigInt.int2bigInt

class Writer(val plugin: Plugin) {

  private val root = new File(plugin.getDataFolder, "islands")
  private val nbtManager = SpigotMetadataAPI.getAPI.getNBTManager

  def write(island: RawIsland): Unit = {
    val folders = createFolder(island)
    val islandFile = createFile(folders(0), island)
    val outputStream = new FileOutputStream(islandFile)
    write(outputStream, "skbl")
    writeVersion(outputStream, island.minecraftVersion)
    writeBlocks(outputStream, island.blocks)
  }

  private def writeVersion(outputStream: FileOutputStream, version: Version): Unit = {
    writeU1Int(outputStream, version.major)
    writeU1Int(outputStream, version.minor)
    writeU1Int(outputStream, version.revision)
  }

  private def writeBlocks(outputStream: FileOutputStream, blocks: List[Block]): Unit = {
    val reducedBlocks = Writer.reduceList(blocks.map(new NotLocateBlock(_)))
    val blockEntities = Writer.reduceList(reducedBlocks.filter(BlockEntity.isBlockEntity))
    writeU2Int(outputStream, blockEntities.length)
    blockEntities.foreach(block => {
      val location = block.block.getLocation
      val name = block.getType + "" + (location.getBlockX + location.getBlockY + location.getBlockZ)
      writeString(outputStream, name.toLowerCase)
    })

    writeU2Int(outputStream, reducedBlocks.length)
    reducedBlocks.foreach(block => {
      val fullName = block.getType.name().toLowerCase
      writeString(outputStream, fullName)
      //todo states
      if (BlockEntity.isBlockEntity(block.block)) writeU2Int(outputStream, blockEntities.indexOf(block))
      else write(outputStream, Array(0xFF.asInstanceOf[Byte], 0xFF.asInstanceOf[Byte]))
    })
    //todo
  }

  private def write(outputStream: FileOutputStream, string: String): Unit = write(outputStream, string.getBytes)

  private def writeString(outputStream: FileOutputStream, string: String): Unit = {
    writeU1Int(outputStream, string.getBytes.length)
    write(outputStream, string)
  }

  private def writeU1Int(outputStream: FileOutputStream, int: Int): Unit = outputStream.write(int)

  private def writeU2Int(outputStream: FileOutputStream, int: Int): Unit = {
    val list = int.toByteArray
    val result: List[Byte] = list.length match {
      case 0 => List[Byte](0, 0)
      case 1 => 0x00 :: list.toList
      case 2 => list.toList
      case _ => list.takeRight(2).toList
    }
    write(outputStream, result.toArray)
  }

  private def write(outputStream: FileOutputStream, byte: Byte): Unit = outputStream.write(byte)

  private def write(outputStream: FileOutputStream, bytes: Array[Byte]): Unit = outputStream.write(bytes)

  private def createFolder(island: RawIsland): Array[File] = {
    createIslandsRoot()
    val islandRoot = new File(root, island.name)
    islandRoot.mkdirs()
    val nbt = new File(islandRoot, "nbt")
    nbt.mkdirs()
    Array(islandRoot, nbt)
  }

  private def createFile(islandRoot: File, island: RawIsland): File = {
    val islandFile = new File(islandRoot, s"${island.name}.island")
    if  (!islandFile.exists()) islandFile.createNewFile()
    islandFile
  }

  private def createIslandsRoot(): Unit = {
    plugin.getDataFolder.mkdirs()
    root.mkdirs()
  }
}

case object Writer {

  def reduceList[A](list: List[A]): List[A] = list.foldRight(list)((value, acc) => reduceList(value, acc))

  def reduceList[A](element: A, list: List[A]): List[A] =
    list.foldLeft(List[A]())((acc, value) =>
      if (value != element) value :: acc
      else if (acc.contains(element)) acc
      else value :: acc
    ).reverse
}