package fr.warzou.skyblock.adapter.api

import fr.warzou.skyblock.adapter.api.AdapterAPI.alreadyInitialized
import fr.warzou.skyblock.adapter.api.handler.AdapterHandler
import fr.warzou.skyblock.adapter.api.plugin.MinecraftPlugin
import fr.warzou.skyblock.adapter.api.world.Location

class AdapterAPI(val adapterHandler: AdapterHandler) {

  {
    if (alreadyInitialized) throw new IllegalStateException("Adapter API has already been initialized !")
    alreadyInitialized = true
  }

  def getPlugin: MinecraftPlugin = this.adapterHandler.minecraftPlugin()

  def createLocation(x: Double, y: Double, z: Double): Location = createLocation(None, x, y, z)

  def createLocation(world: Option[String], x: Double, y: Double, z: Double): Location = adapterHandler.createLocation(world, x, y, z)

  def getEntitiesGetter: EntitiesGetter = adapterHandler.getEntitiesGetter
}

private case object AdapterAPI {
  private var alreadyInitialized = false
}
