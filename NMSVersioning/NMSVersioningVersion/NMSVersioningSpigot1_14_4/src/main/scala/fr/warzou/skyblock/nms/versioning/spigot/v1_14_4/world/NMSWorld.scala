package fr.warzou.skyblock.nms.versioning.spigot.v1_14_4.world

import fr.warzou.skyblock.nms.versioning.api.core.world
import fr.warzou.skyblock.nms.versioning.api.core.world._
import fr.warzou.skyblock.nms.versioning.api.core.world.chunk.NMSChunk
import net.minecraft.server.v1_14_R1.{ChunkCoordIntPair, RegionFile, RegionFileCache}
import org.bukkit.World
import org.bukkit.craftbukkit.v1_14_R1.CraftWorld

import java.io.File

case class NMSWorld(_world: World, worldType: WorldType) extends world.NMSWorld {
  private val craftWorld = _world.asInstanceOf[CraftWorld]
  private val nms = craftWorld.getHandle
  private val _level = new File(directory, "level.dat")

  override def directory: File = nms.getDataManager.getDirectory

  override def level: File = _level

  override def regionFolder: File = worldType match {
    case Nether() => new File(directory, s"DIM-1${File.separator}region")
    case TheEnd() => new File(directory, s"DIM1${File.separator}region")
    case Overworld() | Custom(_, _) => new File(directory, "region")
  }

  override def chunkExist(file: File, x: Int, z: Int): Boolean =
    nms.getChunkProvider.isLoaded(x, z) || nms.getChunkProvider.playerChunkMap.chunkExists(new ChunkCoordIntPair(x, z))

  override def chunkExist(file: File, x: Int, y: Int, z: Int): Boolean = chunkExist(file, x, z)

  override def getChunk(x: Int, z: Int): NMSChunk = ???

  override def getChunk(x: Int, y: Int, z: Int): NMSChunk = ???
}
