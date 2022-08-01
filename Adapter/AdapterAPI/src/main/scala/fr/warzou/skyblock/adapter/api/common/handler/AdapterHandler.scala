package fr.warzou.skyblock.adapter.api.common.handler

import fr.warzou.skyblock.adapter.api.core.entity.EntitiesGetter
import fr.warzou.skyblock.adapter.api.core.plugin.MinecraftPlugin
import fr.warzou.skyblock.adapter.api.core.world.Location

abstract case class AdapterHandler() {

  def minecraftPlugin(): MinecraftPlugin

  def createLocation(world: Option[String], x: Double, y: Double, z: Double): Location

  def getEntitiesGetter: EntitiesGetter

}
