package fr.warzou.skyblock.nms.versioning.spigot.v1_12_2.world.chunk

import fr.warzou.skyblock.nms.versioning.api.core.world.chunk.{NMSChunk, NMSChunkSection}
import fr.warzou.skyblock.nms.versioning.api.core.world.chunk
import fr.warzou.skyblock.nms.versioning.spigot.v1_12_2.world.NMSWorld
import org.bukkit.craftbukkit.v1_12_R1.CraftChunk

case class NMSChunk(world: NMSWorld, x: Int, y: Int) extends chunk.NMSChunk {
  private val minecraftWorld = world._world
  private val nmsMinecraftWorld = world.nms
  private val nmsChunk = nmsMinecraftWorld.getChunkProviderServer.getChunkAt(x, y)
  private val craftChunk = nmsChunk.bukkitChunk.asInstanceOf[CraftChunk]
  private val nmsSections = nmsChunk.getSections
  private val sections = new Array[NMSChunkSection](nmsSections.length)

  override def isLoaded: Boolean = craftChunk.isLoaded

  override def getSections: Array[NMSChunkSection] = ???

  override def toCustomChunkFormat: Array[Byte] = ???
}