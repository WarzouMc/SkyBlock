package fr.warzou.skyblock.adapter.api

import fr.warzou.skyblock.adapter.api.AdapterAPI.alreadyInitialized
import fr.warzou.skyblock.adapter.api.common.handler.AdapterHandler
import fr.warzou.skyblock.adapter.api.common.logger.Logger
import fr.warzou.skyblock.adapter.api.common.wrap.{Unwrapper, Wrappable, Wrapper}
import fr.warzou.skyblock.adapter.api.core.entity.{EntitiesGetter, Entity}
import fr.warzou.skyblock.adapter.api.core.plugin.MinecraftPlugin
import fr.warzou.skyblock.adapter.api.core.world.Location
import fr.warzou.skyblock.utils.server.{ServerAPI, Spigot}

case class AdapterAPI(adapterHandler: AdapterHandler) {

  {
    if (alreadyInitialized) throw new IllegalStateException("Adapter API has already been initialized !")
    alreadyInitialized = true
  }

  def plugin: MinecraftPlugin = adapterHandler.minecraftPlugin()

  def logger: Logger = plugin.logger

  def createLocation(x: Double, y: Double, z: Double): Location = createLocation(None, x, y, z)

  def createLocation(world: String, x: Double, y: Double, z: Double): Location = adapterHandler.createLocation(Some(world), x, y, z)

  def createLocation(world: Option[String], x: Double, y: Double, z: Double): Location = adapterHandler.createLocation(world, x, y, z)

  def entitiesGetter(): EntitiesGetter = adapterHandler.getEntitiesGetter

  def wrapperOf[A](wrappable: Wrappable[A]): Wrapper[_, A] = wrapperOf[A](wrappable.getClass)

  def wrapperOf[A](clazz: Class[_ <: Wrappable[A]]): Wrapper[_ >: Any, A] = adapterHandler.wrapperOf(clazz)

  def unwrapperOf[A](wrappable: Wrappable[A]): Unwrapper[A, _] = unwrapperOf[A](wrappable.getClass)

  def unwrapperOf[A](clazz: Class[_ <: Wrappable[A]]): Unwrapper[A, _ >: Any] = adapterHandler.unwrapperOf(clazz)
}

case object AdapterAPI {
  private var alreadyInitialized = false

  def createAdapter(serverAPI: ServerAPI, plugin: Object): AdapterAPI = {
    serverAPI match {
      case Spigot() => Class.forName("fr.warzou.skyblock.adapter.spigot.SpigotAdapter")
        .getConstructors.apply(0).newInstance(plugin).asInstanceOf[AdapterClass].adapter
      case _ => throw new IllegalArgumentException("Unknown server api")
    }
  }
}
