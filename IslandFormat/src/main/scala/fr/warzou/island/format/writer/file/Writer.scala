package fr.warzou.island.format.writer.file

import fr.il_totore.spigotmetadata.api.SpigotMetadataAPI
import fr.il_totore.spigotmetadata.api.nbt.{NBTOutputStream, NBTTagCompound}
import fr.warzou.island.format.core.common.Version
import fr.warzou.island.format.core.common.block.NotLocateBlock
import fr.warzou.island.format.core.common.block.tileentities.BlockEntity
import fr.warzou.island.format.core.common.cuboid.Cuboid
import fr.warzou.skyblock.utils.{ArrayUtils, IOUtils}
import org.bukkit.Location
import org.bukkit.block.Block
import org.bukkit.entity.Entity

import java.io.{ByteArrayOutputStream, OutputStream}
import java.nio.ByteBuffer
import java.nio.charset.StandardCharsets

protected class Writer(outputStream: OutputStream, minecraftVersion: Version, cuboid: Cuboid, blocks: List[Block], entities: List[Entity]) {

  private val nbtManager = SpigotMetadataAPI.getAPI.getNBTManager

  def write(): Unit = {
    writeVersion(minecraftVersion)
    val notLocateBlocks = writeBlocks(blocks)
    writeCuboid(cuboid, blocks, notLocateBlocks)
    writeEntities(entities, cuboid)
  }

  private def writeVersion(version: Version): Unit = {
    writeU1Int(version.major)
    writeU1Int(version.minor)
    writeU1Int(version.revision)
  }

  private def writeBlocks(blocks: List[Block]): List[NotLocateBlock] = {
    val reducedBlocks = ArrayUtils.reduceList(blocks.map(new NotLocateBlock(_)))
    val blockEntities = ArrayUtils.reduceList(reducedBlocks.filter(BlockEntity.isBlockEntity))
    writeU2Int(blockEntities.length + 1)
    blockEntities.foreach(writeBlockState)

    writeU2Int(reducedBlocks.length)
    reducedBlocks.foreach(block => {
      val fullName = block.getType.name().toLowerCase
      writeString(fullName)
      writeU1Int(block.getData)

      if (BlockEntity.isBlockEntity(block)) writeU2Int(blockEntities.indexOf(block))
      else writeU2Int(blockEntities.length + 1)
    })
    reducedBlocks
  }

  private def writeBlockState(block: NotLocateBlock): Unit =
    writeNBT(nbtManager.getNBTTag(block.block))

  private def writeCuboid(cuboid: Cuboid, blocks: List[Block], notLocateBlocks: List[NotLocateBlock]): Unit = {
    writeU1Int(cuboid.xSize)
    writeU1Int(cuboid.zSize)
    writeU1Int(cuboid.ySize)

    blocks.map(new NotLocateBlock(_)).foreach(block => IOUtils.writeVarInt(outputStream, notLocateBlocks.indexOf(block), (byte: Byte) => write(byte)))
  }

  private def writeEntities(entities: List[Entity], cuboid: Cuboid): Unit = {
    writeU1Int(entities.length)
    entities.foreach(entity => {
      writeLocation(entity.getLocation, cuboid, writeU8Long)
      writeU1Int(entity.getType.ordinal())
      writeNBT(nbtManager.getNBTTag(entity))
    })
  }

  private def writeNBT(nbt: NBTTagCompound): Unit = {
    val byteArrayOutputStream = new ByteArrayOutputStream()
    val nbtOutputStream = new NBTOutputStream(nbtManager, byteArrayOutputStream)
    nbtOutputStream.writeTag(nbt)
    val array = byteArrayOutputStream.toByteArray
    writeU2Int(array.length)
    write(array)
  }

  private def writeLocation(location: Location, cuboid: Cuboid, u8Writer: Long => Unit): Unit = {
    val x = location.getBlockX - cuboid.minX
    val y = location.getBlockY - cuboid.minY
    val z = location.getBlockZ - cuboid.minZ
    val long = ((x & 0x3FFFFFF) << 38) | ((z & 0x3FFFFFF) << 12) | (y & 0xFFF)
    u8Writer(long)
  }

  private def write(string: String): Unit = write(string.getBytes(StandardCharsets.UTF_8))

  private def writeString(string: String): Unit = {
    writeU1Int(string.getBytes.length)
    write(string)
  }

  private def writeU1Int(int: Int): Unit = outputStream.write(int)

  private def writeU2Int(int: Int): Unit = write(ByteBuffer.allocate(2).putShort(int.toShort).array())

  private def writeU8Long(long: Long): Unit = write(ByteBuffer.allocate(8).putLong(long).array())

  private def write(byte: Byte): Unit = outputStream.write(byte)

  private def write(bytes: Array[Byte]): Unit = outputStream.write(bytes)
}