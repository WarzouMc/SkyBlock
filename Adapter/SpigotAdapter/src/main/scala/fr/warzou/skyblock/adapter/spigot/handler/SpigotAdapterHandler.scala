package fr.warzou.skyblock.adapter.spigot.handler

import fr.warzou.skyblock.adapter.api.common.handler.AdapterHandler
import fr.warzou.skyblock.adapter.api.common.wrap.{Unwrapper, Wrappable, Wrapper}
import fr.warzou.skyblock.adapter.api.core.entity.{EntitiesGetter, Entity}
import fr.warzou.skyblock.adapter.api.core.plugin.MinecraftPlugin
import fr.warzou.skyblock.adapter.api.core.world.{Block, Location}
import fr.warzou.skyblock.adapter.spigot.entity.{SpigotEntitiesGetter, SpigotEntity}
import fr.warzou.skyblock.adapter.spigot.plugin.SpigotPlugin
import fr.warzou.skyblock.adapter.spigot.world.{SpigotBlock, SpigotLocation}
import org.bukkit.plugin.Plugin

case class SpigotAdapterHandler(plugin: Plugin) extends AdapterHandler {

  private val spigotPlugin = SpigotPlugin(plugin)
  private val entitiesGetter = SpigotEntitiesGetter

  override def minecraftPlugin(): MinecraftPlugin = spigotPlugin

  override def createLocation(world: Option[String], x: Double, y: Double, z: Double): Location = SpigotLocation(world, x, y, z)

  override def getEntitiesGetter: EntitiesGetter = entitiesGetter

  override def wrapperOf[A](wrappable: Wrappable[A]): Wrapper[_, A] = {
    (if (wrappable.getClass.isAssignableFrom(classOf[Entity])) SpigotEntity
    else if (wrappable.getClass.isAssignableFrom(classOf[Block])) SpigotBlock
    else if (wrappable.getClass.isAssignableFrom(classOf[Location])) SpigotLocation
    else throw new UnsupportedOperationException("Unsupported class !")).asInstanceOf[Wrapper[_, A]]
  }

  override def unwrapperOf[A](wrappable: Wrappable[A]): Unwrapper[A, _] = {
    (if (wrappable.getClass.isAssignableFrom(classOf[Entity])) SpigotEntity
    else if (wrappable.getClass.isAssignableFrom(classOf[Block])) SpigotBlock
    else if (wrappable.getClass.isAssignableFrom(classOf[Location])) SpigotLocation
    else throw new UnsupportedOperationException("Unsupported class !")).asInstanceOf[Unwrapper[A, _]]
  }
}