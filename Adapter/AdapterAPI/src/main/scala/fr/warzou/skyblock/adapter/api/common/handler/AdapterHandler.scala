package fr.warzou.skyblock.adapter.api.common.handler

import fr.warzou.skyblock.adapter.api.common.wrap.{Unwrapper, Wrappable, Wrapper}
import fr.warzou.skyblock.adapter.api.core.entity.EntitiesGetter
import fr.warzou.skyblock.adapter.api.core.plugin.MinecraftPlugin
import fr.warzou.skyblock.adapter.api.core.world.Location

abstract class AdapterHandler {
  def minecraftPlugin(): MinecraftPlugin

  def createLocation(world: Option[String], x: Double, y: Double, z: Double): Location

  def getEntitiesGetter: EntitiesGetter

  def wrapperOf[A](wrappable: Wrappable[A]): Wrapper[_, A]

  def unwrapperOf[A](wrappable: Wrappable[A]): Unwrapper[A, _]
}
