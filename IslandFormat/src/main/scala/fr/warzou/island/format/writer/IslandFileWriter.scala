package fr.warzou.island.format.writer

import fr.warzou.island.format.core.io.Writer
import fr.warzou.skyblock.adapter.api.core.entity.Entity
import fr.warzou.skyblock.adapter.api.core.plugin.MinecraftPlugin
import fr.warzou.skyblock.adapter.api.core.world.{Block, Location}
import fr.warzou.skyblock.utils.ServerVersion
import fr.warzou.skyblock.utils.collection.ArrayUtils
import fr.warzou.skyblock.utils.cuboid.Cuboid

import java.io.OutputStream
import java.nio.ByteBuffer
import java.nio.charset.StandardCharsets
import java.util.UUID

case class IslandFileWriter(outputStream: OutputStream, plugin: MinecraftPlugin, version: ServerVersion, uuid: UUID,
                            name: String, cuboid: Cuboid, blocks: List[Block], entities: List[Entity]) extends Writer {

  override def write(): Unit = {
    writeVersion()
    writeInfo()
    val reducedBlocks = writeBlocks(blocks)
    writeCuboid(cuboid, reducedBlocks)
    writeEntities(entities, cuboid)
    outputStream.close()
    plugin.logger.io(s"Success save of island '$uuid'.")
  }

  private def writeInfo(): Unit = {
    val byteBuffer = ByteBuffer.allocate(16)
    byteBuffer.putLong(uuid.getMostSignificantBits)
    byteBuffer.putLong(uuid.getLeastSignificantBits)
    write(byteBuffer.array())
    writeString(name)
  }

  private def writeVersion(): Unit = {
    write(version.major)
    write(version.minor)
    write(version.revision)
  }

  private def writeBlocks(blocks: List[Block]): List[Block] = {
    val reducedBlocks = ArrayUtils.reduceList(blocks)
    val blockEntities = ArrayUtils.reduceList(reducedBlocks.filter(_.isBlockEntity))
    write(blockEntities.length + 1)
    blockEntities.map(_.nbt.get).foreach(writeArray)

    write(reducedBlocks.length)
    reducedBlocks.foreach(block => {
      val fullName = block.name
      writeString(fullName)
      write(block.data)

      if (block.isBlockEntity) write(blockEntities.indexOf(block))
      else write(blockEntities.length + 1)
    })
    reducedBlocks
  }

  private def writeCuboid(cuboid: Cuboid, reducedBlocks: List[Block]): Unit = {
    write(cuboid.xSize)
    write(cuboid.zSize)
    write(cuboid.ySize)

    blocks.foreach(block => write(reducedBlocks.indexOf(block)))
  }

  private def writeEntities(entities: List[Entity], cuboid: Cuboid): Unit = {
    write(entities.length)
    entities.foreach(entity => {
      writeLocation(entity.location, cuboid)
      writeString(entity.name)
      writeArray(entity.nbt)
    })
  }

  private def writeLocation(location: Location, cuboid: Cuboid): Unit = {
    val x = location.blockX - cuboid.minX
    val y = location.blockY - cuboid.minY
    val z = location.blockZ - cuboid.minZ
    write(x)
    write(y)
    write(z)
  }

  private def write(string: String): Unit = write(string.getBytes(StandardCharsets.UTF_8))

  private def writeString(string: String): Unit = {
    write(string.getBytes.length)
    write(string)
  }

  private def writeU2Int(int: Int): Unit = write(ByteBuffer.allocate(2).putShort(int.toShort).array())

  private def writeU8Long(long: Long): Unit = write(ByteBuffer.allocate(8).putLong(long).array())

  private def writeArray(bytes: Array[Byte]): Unit = {
    writeU2Int(bytes.length)
    write(bytes)
  }

  private def write(byte: Int): Unit = outputStream.write(byte)

  private def write(bytes: Array[Byte]): Unit = outputStream.write(bytes)
}