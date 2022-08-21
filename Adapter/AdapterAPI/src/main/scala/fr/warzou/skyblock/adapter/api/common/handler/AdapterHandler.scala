package fr.warzou.skyblock.adapter.api.common.handler

import fr.warzou.skyblock.adapter.api.AdapterAPI
import fr.warzou.skyblock.adapter.api.common.wrap.{Unwrapper, Wrappable, Wrapper}
import fr.warzou.skyblock.adapter.api.core.entity.EntitiesGetter
import fr.warzou.skyblock.adapter.api.core.plugin.MinecraftPlugin
import fr.warzou.skyblock.adapter.api.core.world.location.Location

abstract class AdapterHandler {

  /**
   * Returns used [[MinecraftPlugin]] implementation.
   * @return used [[MinecraftPlugin]]
   */
  def minecraftPlugin(): MinecraftPlugin

  /**
   * Returns a new [[Location]].
   * @param world optionally world name
   * @param x x location
   * @param y y location
   * @param z z location
   * @return a new [[Location]]
   */
  def createLocation(world: Option[String], x: Double, y: Double, z: Double): Location

  /**
   * Return an EntitiesGetter.
   * @return an EntityGetter
   */
  def getEntitiesGetter(adapter: AdapterAPI): EntitiesGetter

  /**
   * Get the wrapper to the type A.
   * @param clazz class of a wrappable object
   * @tparam A to type
   * @return a wrapper to type A
   */
  def wrapperOf[A <: Wrappable[A]](clazz: Class[_ <: Wrappable[A]]): Wrapper[_ >: Any, A]

  /**
   * Get the unwrapper from the type A.
   * @param clazz class of a wrappable object
   * @tparam A from type
   * @return a unwrapper from type A
   */
  def unwrapperOf[A <: Wrappable[A]](clazz: Class[_ <: Wrappable[A]]): Unwrapper[A, _ >: Any]
}
