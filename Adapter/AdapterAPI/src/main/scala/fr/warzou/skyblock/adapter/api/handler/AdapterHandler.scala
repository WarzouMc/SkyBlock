package fr.warzou.skyblock.adapter.api.handler

import fr.warzou.skyblock.adapter.api.entity.EntitiesGetter
import fr.warzou.skyblock.adapter.api.plugin.MinecraftPlugin
import fr.warzou.skyblock.adapter.api.world.Location

abstract class AdapterHandler {

  def minecraftPlugin(): MinecraftPlugin

  def createLocation(world: Option[String], x: Double, y: Double, z: Double): Location

  def getEntitiesGetter: EntitiesGetter

}
