package fr.warzou.skyblock.nms.versioning.spigot.v1_13_2.world

import fr.warzou.skyblock.nms.versioning.api.world
import org.bukkit.World
import org.bukkit.craftbukkit.v1_13_R2.CraftWorld

import java.io.File

case class NMSWorld(_world: World) extends world.NMSWorld {
  private val craftWorld = _world.asInstanceOf[CraftWorld]
  private val nms = craftWorld.getHandle

  override def directory: File = nms.getDataManager.getDirectory
}
