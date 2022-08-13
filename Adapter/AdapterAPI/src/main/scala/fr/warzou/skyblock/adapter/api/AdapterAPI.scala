package fr.warzou.skyblock.adapter.api

import fr.warzou.skyblock.adapter.api.AdapterAPI.{alreadyInitialized, createAdapter}
import fr.warzou.skyblock.adapter.api.common.handler.AdapterHandler
import fr.warzou.skyblock.adapter.api.common.logger.Logger
import fr.warzou.skyblock.adapter.api.common.wrap.{Unwrapper, Wrappable, Wrapper}
import fr.warzou.skyblock.adapter.api.core.entity.{EntitiesGetter, Entity}
import fr.warzou.skyblock.adapter.api.core.plugin.MinecraftPlugin
import fr.warzou.skyblock.adapter.api.core.world.Location
import fr.warzou.skyblock.utils.cuboid.Cuboid
import fr.warzou.skyblock.utils.server.{ServerAPI, Spigot}

/**
 * This class contain all API's getters.
 * That allow to wrap some things, like minecraft block or entity, for a unique IslandFormat implementation.
 *
 * @param adapterHandler an AdapterHandler impl
 *
 * @version 0.0.1
 * @author Warzou
 */
case class AdapterAPI(adapterHandler: AdapterHandler) {

  {
    if (alreadyInitialized) throw new IllegalStateException("Adapter API has already been initialized !")
    alreadyInitialized = true
  }

  /**
   * Returns used [[MinecraftPlugin]] implementation.
   * @return used [[MinecraftPlugin]]
   */
  def plugin: MinecraftPlugin = adapterHandler.minecraftPlugin()

  /**
   * Returns used [[Logger]] implementation.
   * @return used [[Logger]]
   */
  def logger: Logger = plugin.logger

  /**
   * Returns a new [[Location]].
   * @param x x location
   * @param y y location
   * @param z z location
   * @return a new [[Location]]
   */
  def createLocation(x: Double, y: Double, z: Double): Location = createLocation(None, x, y, z)

  /**
   * Returns a new [[Location]].
   * @param world world name
   * @param x x location
   * @param y y location
   * @param z z location
   * @return a new [[Location]]
   */
  def createLocation(world: String, x: Double, y: Double, z: Double): Location = adapterHandler.createLocation(Some(world), x, y, z)

  /**
   * Returns a new [[Location]].
   * @param world optionally world name
   * @param x x location
   * @param y y location
   * @param z z location
   * @return a new [[Location]]
   */
  def createLocation(world: Option[String], x: Double, y: Double, z: Double): Location = adapterHandler.createLocation(world, x, y, z)

  /**
   * Return an EntitiesGetter.
   * @return an EntityGetter
   */
  def entitiesGetter: EntitiesGetter = adapterHandler.getEntitiesGetter(this)

  /**
   * Get the wrapper to the type A.
   * @param wrappable wrappable object
   * @tparam A to type
   * @return a wrapper to A type
   */
  def wrapperOf[A](wrappable: Wrappable[A]): Wrapper[_ >: Any, A] = wrapperOf[A](wrappable.getClass)

  /**
   * Get the wrapper to the type A.
   * @param clazz class of a wrappable object
   * @tparam A to type
   * @return a wrapper to type A
   */
  def wrapperOf[A](clazz: Class[_ <: Wrappable[A]]): Wrapper[_ >: Any, A] = adapterHandler.wrapperOf(clazz)

  /**
   * Get the unwrapper from the type A.
   * @param wrappable wrappable object
   * @tparam A from type
   * @return a unwrapper from type A
   */
  def unwrapperOf[A](wrappable: Wrappable[A]): Unwrapper[A, _ >: Any] = unwrapperOf[A](wrappable.getClass)

  /**
   * Get the unwrapper from the type A.
   * @param clazz class of a wrappable object
   * @tparam A from type
   * @return a unwrapper from type A
   */
  def unwrapperOf[A](clazz: Class[_ <: Wrappable[A]]): Unwrapper[A, _ >: Any] = adapterHandler.unwrapperOf(clazz)
}

case object AdapterAPI {
  private var alreadyInitialized = false

  /**
   * Create a new AdapterAPI from a serverAPI.
   * @param serverAPI a serverAPI
   * @param plugin used api plugin class (JavaPlugin for Bukkit, ...)
   * @return a new AdapterAPI if doesn't exist for serverAPI
   */
  def createAdapter(serverAPI: ServerAPI, plugin: Object): AdapterAPI = {
    serverAPI match {
      case Spigot() => Class.forName("fr.warzou.skyblock.adapter.spigot.SpigotAdapter")
        .getConstructors.apply(0).newInstance(plugin).asInstanceOf[AdapterClass].adapter
      case _ => throw new IllegalArgumentException("Unknown server api")
    }
  }
}
