package fr.warzou.skyblock.adapter.spigot.handler

import fr.warzou.skyblock.adapter.api.entity.EntitiesGetter
import fr.warzou.skyblock.adapter.api.handler.AdapterHandler
import fr.warzou.skyblock.adapter.api.plugin.MinecraftPlugin
import fr.warzou.skyblock.adapter.api.world.Location
import fr.warzou.skyblock.adapter.spigot.entity.{SpigotEntity, SpigotEntitiesGetter}
import fr.warzou.skyblock.adapter.spigot.plugin.SpigotPlugin
import fr.warzou.skyblock.adapter.spigot.world.SpigotLocation
import org.bukkit.plugin.Plugin

case class SpigotAdapterHandler(plugin: Plugin) extends AdapterHandler {

  private val spigotPlugin = SpigotPlugin(plugin)
  private val entitiesGetter = SpigotEntitiesGetter

  override def minecraftPlugin(): MinecraftPlugin = spigotPlugin

  override def createLocation(world: Option[String], x: Double, y: Double, z: Double): Location = SpigotLocation(world, x, y, z)

  override def getEntitiesGetter: EntitiesGetter = entitiesGetter
}