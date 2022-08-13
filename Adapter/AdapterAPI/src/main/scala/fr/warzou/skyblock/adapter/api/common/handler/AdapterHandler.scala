package fr.warzou.skyblock.adapter.api.common.handler

import fr.warzou.skyblock.adapter.api.AdapterAPI
import fr.warzou.skyblock.adapter.api.common.wrap.{Unwrapper, Wrappable, Wrapper}
import fr.warzou.skyblock.adapter.api.core.entity.EntitiesGetter
import fr.warzou.skyblock.adapter.api.core.plugin.MinecraftPlugin
import fr.warzou.skyblock.adapter.api.core.world.Location

abstract class AdapterHandler {
  def minecraftPlugin(): MinecraftPlugin

  def createLocation(world: Option[String], x: Double, y: Double, z: Double): Location

  def getEntitiesGetter(adapter: AdapterAPI): EntitiesGetter

  def wrapperOf[A](clazz: Class[_ <: Wrappable[A]]): Wrapper[_ >: Any, A]

  def unwrapperOf[A](clazz: Class[_ <: Wrappable[A]]): Unwrapper[A, _ >: Any]
}
