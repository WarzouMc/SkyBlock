package fr.warzou.skyblock.adapter.api

import fr.warzou.skyblock.adapter.api.AdapterAPI.alreadyInitialized
import fr.warzou.skyblock.adapter.api.entity.EntitiesGetter
import fr.warzou.skyblock.adapter.api.handler.AdapterHandler
import fr.warzou.skyblock.adapter.api.plugin.MinecraftPlugin
import fr.warzou.skyblock.adapter.api.world.Location
import fr.warzou.skyblock.utils.server.{ServerAPI, Spigot}

class AdapterAPI(val adapterHandler: AdapterHandler) {

  {
    if (alreadyInitialized) throw new IllegalStateException("Adapter API has already been initialized !")
    alreadyInitialized = true
  }

  def plugin: MinecraftPlugin = adapterHandler.minecraftPlugin()

  def createLocation(x: Double, y: Double, z: Double): Location = createLocation(None, x, y, z)

  def createLocation(world: String, x: Double, y: Double, z: Double): Location = adapterHandler.createLocation(Some(world), x, y, z)

  def createLocation(world: Option[String], x: Double, y: Double, z: Double): Location = adapterHandler.createLocation(world, x, y, z)

  def entitiesGetter: EntitiesGetter = adapterHandler.getEntitiesGetter
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
