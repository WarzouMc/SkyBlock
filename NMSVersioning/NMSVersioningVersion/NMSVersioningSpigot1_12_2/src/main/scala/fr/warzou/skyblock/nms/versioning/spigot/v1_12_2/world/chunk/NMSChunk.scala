package fr.warzou.skyblock.nms.versioning.spigot.v1_12_2.world.chunk

import fr.warzou.skyblock.nms.versioning.api.NMSVersioningAPI
import fr.warzou.skyblock.nms.versioning.api.core.entity.EntityWrap
import fr.warzou.skyblock.nms.versioning.api.core.world.chunk
import fr.warzou.skyblock.nms.versioning.api.core.world.chunk.NMSChunkSection
import fr.warzou.skyblock.nms.versioning.spigot.v1_12_2.block.BlockWrap
import fr.warzou.skyblock.nms.versioning.spigot.v1_12_2.world.NMSWorld
import fr.warzou.skyblock.utils.ServerVersion
import fr.warzou.skyblock.utils.server.Spigot
import net.jpountz.lz4.LZ4FrameOutputStream
import net.minecraft.server.v1_12_R1.Block
import org.apache.commons.io.output.ByteArrayOutputStream
import org.bukkit.craftbukkit.v1_12_R1.CraftChunk

import java.io.DataOutputStream
import java.nio.ByteBuffer
import scala.collection.mutable.ListBuffer

case class NMSChunk(world: NMSWorld, x: Int, y: Int) extends chunk.NMSChunk {
  private val minecraftWorld = world._world
  private val nmsMinecraftWorld = world.nms
  private val nmsChunk = nmsMinecraftWorld.getChunkProviderServer.getChunkAt(x, y)
  private val craftChunk = nmsChunk.bukkitChunk.asInstanceOf[CraftChunk]
  private val nbtTool = NMSVersioningAPI.getVersionAPI(Spigot(), ServerVersion.v1_12_2).getNBTTools

  override def isLoaded: Boolean = craftChunk.isLoaded

  override val sections: Array[NMSChunkSection] = nmsChunk.getSections.filterNot(_ == null).map(NMSChunkSection(_, nmsChunk))

  override def entities: (Int, Array[Byte]) = {
    val entities_ = craftChunk.getEntities

    (entities_.length, entities_.map(entity => nbtTool.parse(EntityWrap.of(entity)))
      .flatMap(
        bytes => new ListBuffer[Byte]
          .addAll(ByteBuffer.allocate(2).putShort(bytes.length.toShort).array())
          .addAll(bytes).toArray
      ))
  }

  override def tileEntities: (Int, Array[Byte]) = {
    val tileEntities_ = craftChunk.getTileEntities

    (tileEntities_.length, tileEntities_.map(_.getBlock)
      .map(block => nbtTool.parse(BlockWrap(block)))
      .flatMap(
        bytes => new ListBuffer[Byte]
          .addAll(ByteBuffer.allocate(2).putShort(bytes.length.toShort).array())
          .addAll(bytes).toArray
      ))
  }

  override def tileTicks: (Int, Array[Byte]) = {
    val list = nmsChunk.world.getWorld.getHandle.a(nmsChunk, false)
    if (list == null)
      (0, Array.emptyByteArray)
    else {
      val outputStream = new ByteArrayOutputStream()
      val dataOutputStream = new DataOutputStream(outputStream)

      list.forEach(tileTick => {
        val worldTime = world._world.getTime
        val minecraftKey = Block.REGISTRY.b(tileTick.a())
        val id = if (minecraftKey == null) "" else minecraftKey.toString
        val location = tileTick.a
        val time = tileTick.b - worldTime
        val priority = tileTick.c

        dataOutputStream.writeUTF(id)
        dataOutputStream.write(location.getX)
        dataOutputStream.write(location.getY)
        dataOutputStream.write(location.getZ)
        dataOutputStream.write(time.toInt)
        dataOutputStream.write(priority)
      })

      (list.size(), outputStream.toByteArray)
    }
  }

  override def toCustomChunkFormat: Array[Byte] = {
    val byteArrayOutputStream = new ByteArrayOutputStream()
    val outputStream = new LZ4FrameOutputStream(byteArrayOutputStream)
    val dataOutput = new DataOutputStream(outputStream)

    dataOutput.writeInt(x)
    dataOutput.writeInt(y)
    outputStream.write(nmsChunk.getBiomeIndex)
    nmsChunk.r().foreach(dataOutput.writeInt)
    dataOutput.writeByte(sections.length)
    sections.foreach(section => outputStream.write(section.toByteArray))
    dataOutput.writeShort(entities._1)
    dataOutput.write(entities._2)
    dataOutput.writeShort(tileEntities._1)
    dataOutput.write(tileEntities._2)
    dataOutput.writeShort(tileTicks._1)
    dataOutput.write(tileTicks._2)

    byteArrayOutputStream.toByteArray
  }
}