package fr.warzou.skyblock.nms.versioning.spigot.v1_14_4.world

import fr.warzou.skyblock.nms.versioning.api.world
import org.bukkit.World
import org.bukkit.craftbukkit.v1_14_R1.CraftWorld

import java.io.File

case class NMSWorld(_world: World) extends world.NMSWorld {
  private val craftWorld = _world.asInstanceOf[CraftWorld]
  private val nms = craftWorld.getHandle

  override def directory: File = nms.getDataManager.getDirectory
}
