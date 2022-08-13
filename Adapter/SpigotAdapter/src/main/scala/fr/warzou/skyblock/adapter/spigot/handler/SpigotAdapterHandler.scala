package fr.warzou.skyblock.adapter.spigot.handler

import fr.warzou.skyblock.adapter.api.AdapterAPI
import fr.warzou.skyblock.adapter.api.common.handler.AdapterHandler
import fr.warzou.skyblock.adapter.api.common.wrap.{Unwrapper, Wrappable, Wrapper}
import fr.warzou.skyblock.adapter.api.core.entity.{EntitiesGetter, Entity}
import fr.warzou.skyblock.adapter.api.core.plugin.MinecraftPlugin
import fr.warzou.skyblock.adapter.api.core.world.{Block, Location}
import fr.warzou.skyblock.adapter.spigot.entity.SpigotEntity
import fr.warzou.skyblock.adapter.spigot.plugin.SpigotPlugin
import fr.warzou.skyblock.adapter.spigot.world.{SpigotBlock, SpigotLocation}
import fr.warzou.skyblock.nms.versioning.api.NMSVersioningAPI
import org.bukkit.plugin.Plugin

case class SpigotAdapterHandler(plugin: Plugin) extends AdapterHandler {

  private val spigotPlugin = SpigotPlugin(plugin)

  override def minecraftPlugin(): MinecraftPlugin = spigotPlugin

  override def createLocation(world: Option[String], x: Double, y: Double, z: Double): Location = SpigotLocation(world, x, y, z)

  override def getEntitiesGetter(adapter: AdapterAPI): EntitiesGetter =
    NMSVersioningAPI.getVersionAPI(adapter.plugin).enumerateEntities(adapter, _)
      .map(adapter.wrapperOf[Entity](classOf[Entity]).wrap(_))

  override def wrapperOf[A](clazz: Class[_ <: Wrappable[A]]): Wrapper[_ >: Any, A] = {
    (if (clazz.isAssignableFrom(classOf[Entity])) SpigotEntity
    else if (clazz.isAssignableFrom(classOf[Block])) SpigotBlock
    else if (clazz.isAssignableFrom(classOf[Location])) SpigotLocation
    else throw new UnsupportedOperationException("Unsupported class !")).asInstanceOf[Wrapper[_ >: Any, A]]
  }

  override def unwrapperOf[A](clazz: Class[_ <: Wrappable[A]]): Unwrapper[A, _ >: Any] = {
    (if (clazz.isAssignableFrom(classOf[Entity])) SpigotEntity
    else if (clazz.isAssignableFrom(classOf[Block])) SpigotBlock
    else if (clazz.isAssignableFrom(classOf[Location])) SpigotLocation
    else throw new UnsupportedOperationException("Unsupported class !")).asInstanceOf[Unwrapper[A, _ >: Any]]
  }
}