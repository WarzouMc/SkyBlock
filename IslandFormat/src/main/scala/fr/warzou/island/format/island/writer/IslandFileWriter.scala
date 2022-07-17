package fr.warzou.island.format.island.writer

import fr.warzou.island.format.core.io.Writer
import fr.warzou.skyblock.adapter.api.entity.Entity
import fr.warzou.skyblock.adapter.api.world._
import fr.warzou.skyblock.utils.cuboid.Cuboid
import fr.warzou.skyblock.utils.{ArrayUtils, Version}

import java.io.OutputStream
import java.nio.ByteBuffer
import java.nio.charset.StandardCharsets
import java.util.UUID

protected class IslandFileWriter(outputStream: OutputStream, version: Version, uuid: UUID, name: String, cuboid: Cuboid,
                                 blocks: List[Block], entities: List[Entity]) extends Writer {

  override def write(): Unit = {
    writeVersion()
    writeInfo()
    val reducedBlocks = writeBlocks(blocks)
    writeCuboid(cuboid, reducedBlocks)
    writeEntities(entities, cuboid)
  }

  private def writeInfo(): Unit = {
    val byteBuffer = ByteBuffer.allocate(16)
    byteBuffer.putLong(uuid.getMostSignificantBits)
    byteBuffer.putLong(uuid.getLeastSignificantBits)
    write(byteBuffer.array())
    writeString(name)
  }

  private def writeVersion(): Unit = {
    writeU1Int(version.major)
    writeU1Int(version.minor)
    writeU1Int(version.revision)
  }

  private def writeBlocks(blocks: List[Block]): List[Block] = {
    val reducedBlocks = ArrayUtils.reduceList(blocks)
    val blockEntities = ArrayUtils.reduceList(reducedBlocks.filter(_.isBlockEntity))
    writeU1Int(blockEntities.length + 1)
    blockEntities.map(_.nbt.get).foreach(writeArray)

    writeU1Int(reducedBlocks.length)
    reducedBlocks.foreach(block => {
      val fullName = block.name
      writeString(fullName)
      writeU1Int(block.data)

      if (block.isBlockEntity) writeU1Int(blockEntities.indexOf(block))
      else writeU1Int(blockEntities.length + 1)
    })
    reducedBlocks
  }

  private def writeCuboid(cuboid: Cuboid, reducedBlocks: List[Block]): Unit = {
    writeU1Int(cuboid.xSize)
    writeU1Int(cuboid.zSize)
    writeU1Int(cuboid.ySize)

    blocks.foreach(block => writeU1Int(reducedBlocks.indexOf(block)))
  }

  private def writeEntities(entities: List[Entity], cuboid: Cuboid): Unit = {
    writeU1Int(entities.length)
    entities.foreach(entity => {
      writeLocation(entity.location, cuboid, writeU8Long)
      writeString(entity.name)
      writeArray(entity.nbt)
    })
  }

  private def writeLocation(location: Location, cuboid: Cuboid, u8Writer: Long => Unit): Unit = {
    val x = location.blockX - cuboid.minX
    val y = location.blockY - cuboid.minY
    val z = location.blockZ - cuboid.minZ
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

  private def writeArray(bytes: Array[Byte]): Unit = {
    writeU2Int(bytes.length)
    write(bytes)
  }

  private def write(byte: Byte): Unit = outputStream.write(byte)

  private def write(bytes: Array[Byte]): Unit = outputStream.write(bytes)
}