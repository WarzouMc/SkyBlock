package fr.warzou.skyblock.nms.versioning.spigot.v1_12_2.world

import fr.warzou.skyblock.nms.versioning.api.core.world.chunk.NMSChunk
import fr.warzou.skyblock.nms.versioning.api.core.world._
import org.bukkit.World
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld

import java.io.File
import fr.warzou.skyblock.nms.versioning.api.core.world

case class NMSWorld(_world: World, worldType: WorldType) extends world.NMSWorld {
  private val craftWorld = _world.asInstanceOf[CraftWorld]
  private[world] val nms = craftWorld.getHandle
  private val _level = new File(directory, "level.dat")

  override def directory: File = nms.getDataManager.getDirectory

  override def level: File = _level

  override def regionFolder: File = worldType match {
    case Nether() => new File(directory, s"DIM-1${File.separator}region")
    case TheEnd() => new File(directory, s"DIM1${File.separator}region")
    case Overworld() | Custom(_, _) => new File(directory, "region")
  }

  override def getChunk(x: Int, z: Int): NMSChunk = ???

  override def getChunk(x: Int, y: Int, z: Int): NMSChunk = getChunk(x, y)
}