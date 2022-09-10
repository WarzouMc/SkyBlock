package fr.warzou.skyblock.nms.versioning.spigot.v1_12_2.world.chunk

import fr.warzou.skyblock.nms.versioning.api.core.world.chunk
import fr.warzou.skyblock.nms.versioning.api.core.world.chunk.NMSChunkSection
import fr.warzou.skyblock.nms.versioning.spigot.v1_12_2.world.NMSWorld
import net.jpountz.lz4.LZ4FrameOutputStream
import org.apache.commons.io.output.ByteArrayOutputStream
import org.bukkit.craftbukkit.v1_12_R1.CraftChunk

import java.io.DataOutputStream
import java.util.zip.GZIPOutputStream

case class NMSChunk(world: NMSWorld, x: Int, y: Int) extends chunk.NMSChunk {
  private val minecraftWorld = world._world
  private val nmsMinecraftWorld = world.nms
  private val nmsChunk = nmsMinecraftWorld.getChunkProviderServer.getChunkAt(x, y)
  private val craftChunk = nmsChunk.bukkitChunk.asInstanceOf[CraftChunk]

  override def isLoaded: Boolean = craftChunk.isLoaded

  override val sections: Array[NMSChunkSection] = nmsChunk.getSections.filterNot(_ == null).map(NMSChunkSection(_, nmsChunk))

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

    //todo entities/tileentities/tileticks

    byteArrayOutputStream.toByteArray
  }
}