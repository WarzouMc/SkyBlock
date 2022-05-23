package fr.warzou.island.format.writer.file

import fr.il_totore.spigotmetadata.api.SpigotMetadataAPI
import fr.il_totore.spigotmetadata.api.nbt.NBTOutputStream
import fr.warzou.island.format.core.RawIsland
import fr.warzou.island.format.core.common.Version
import fr.warzou.island.format.core.common.block.NotLocateBlock
import fr.warzou.island.format.core.common.block.tileentities.BlockEntity
import fr.warzou.island.format.core.common.cuboid.Cuboid
import fr.warzou.island.format.writer.file.Writer.writeVarInt
import org.bukkit.block.Block
import org.bukkit.plugin.Plugin

import java.io.{ByteArrayOutputStream, File, FileOutputStream}
import scala.annotation.tailrec
import scala.math.BigInt.int2bigInt

class Writer(val plugin: Plugin) {

  private val root = new File(plugin.getDataFolder, "islands")
  private val nbtManager = SpigotMetadataAPI.getAPI.getNBTManager

  def write(island: RawIsland): Unit = {
    val folder = createFolder(island)
    val islandFile = createFile(folder, island)
    val outputStream = new FileOutputStream(islandFile)
    write(outputStream, "skbl")
    writeVersion(outputStream, island.minecraftVersion)
    val notLocateBlocks = writeBlocks(outputStream, island.blocks)
    writeCuboid(outputStream, island.cuboid, island.blocks, notLocateBlocks)
  }

  private def writeVersion(outputStream: FileOutputStream, version: Version): Unit = {
    writeU1Int(outputStream, version.major)
    writeU1Int(outputStream, version.minor)
    writeU1Int(outputStream, version.revision)
  }

  private def writeBlocks(outputStream: FileOutputStream, blocks: List[Block]): List[NotLocateBlock] = {
    val reducedBlocks = Writer.reduceList(blocks.map(new NotLocateBlock(_)))
    val blockEntities = Writer.reduceList(reducedBlocks.filter(BlockEntity.isBlockEntity))
    writeU2Int(outputStream, blockEntities.length + 1)
    blockEntities.foreach(block => {
      val location = block.block.getLocation
      val name = block.getType + "" + (location.getBlockX + location.getBlockY + location.getBlockZ)
      writeBlockState(outputStream, block)
    })

    writeU2Int(outputStream, reducedBlocks.length)
    reducedBlocks.foreach(block => {
      val fullName = block.getType.name().toLowerCase
      writeString(outputStream, fullName)
      writeU1Int(outputStream, block.getData)

      if (BlockEntity.isBlockEntity(block)) writeU2Int(outputStream, blockEntities.indexOf(block))
      else writeU2Int(outputStream, blockEntities.length + 1)
    })
    reducedBlocks
  }

  private def writeBlockState(outputStream: FileOutputStream, block: NotLocateBlock): Unit = {
    val byteArrayOutputStream = new ByteArrayOutputStream()
    val nbtOutputStream = new NBTOutputStream(nbtManager, byteArrayOutputStream)
    nbtOutputStream.writeTag(nbtManager.getNBTTag(block.block))
    val array = byteArrayOutputStream.toByteArray
    writeU2Int(outputStream, array.length)
    write(outputStream, array)
  }

  private def writeCuboid(outputStream: FileOutputStream, cuboid: Cuboid, blocks: List[Block], notLocateBlocks: List[NotLocateBlock]): Unit = {
    writeU1Int(outputStream, cuboid.xSize())
    writeU1Int(outputStream, cuboid.zSize())
    writeU1Int(outputStream, cuboid.ySize())

    blocks.map(new NotLocateBlock(_)).foreach(block => writeVarInt(outputStream, notLocateBlocks.indexOf(block), (o: FileOutputStream, b: Byte) => write(o, b)))
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

  private def createFolder(island: RawIsland): File = {
    createIslandsRoot()
    val islandRoot = new File(root, island.name)
    islandRoot.mkdirs()
    islandRoot
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

  private val SEGMENT_BITS = 0x7F
  private val CONTINUE_BIT = 0x80

  def reduceList[A](list: List[A]): List[A] = list.foldRight(list)((value, acc) => reduceList(value, acc))

  def reduceList[A](element: A, list: List[A]): List[A] =
    list.foldLeft(List[A]())((acc, value) =>
      if (value != element) value :: acc
      else if (acc.contains(element)) acc
      else value :: acc
    ).reverse

  @tailrec
  def writeVarInt(outputStream: FileOutputStream, value: Int, writer: (FileOutputStream, Byte) => Unit): Unit = {
    if ((value & ~SEGMENT_BITS) == 0) writer(outputStream, value.toByte)
    else {
      writer(outputStream, ((value & SEGMENT_BITS) | CONTINUE_BIT).toByte)
      writeVarInt(outputStream, value >>> 7, writer)
    }
  }
}