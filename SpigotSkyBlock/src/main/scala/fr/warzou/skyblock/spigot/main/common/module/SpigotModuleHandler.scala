package fr.warzou.skyblock.spigot.main.common.module

import fr.warzou.skyblock.adapter.api.AdapterAPI
import fr.warzou.skyblock.api.common.module.{Module, ModuleHandler}
import fr.warzou.skyblock.api.core.island.Island
import fr.warzou.skyblock.api.core.modules.island.IslandModule
import fr.warzou.skyblock.format.island.core.RawIsland
import fr.warzou.skyblock.spigot.main.core.island.SpigotIsland
import fr.warzou.skyblock.spigot.main.core.module.SpigotIslandModule
import fr.warzou.skyblock.utils.cuboid.Cuboid
import fr.warzou.skyblock.utils.{ServerVersion, server}
import org.bukkit.Bukkit
import org.bukkit.plugin.Plugin

import java.util.UUID

case class SpigotModuleHandler(plugin: Plugin) extends ModuleHandler {

  private val adapterAPI = AdapterAPI.createAdapter(server.Spigot(), plugin)
  private val serverVersion = ServerVersion.from(adapterAPI.plugin)

  override def adapter: AdapterAPI = adapterAPI

  override def enableAllModules(): Unit = {
    enableModule(SpigotIslandModule(this, adapterAPI))
  }

  override def disableAllModules(): Unit = {
    _modules.foreach(disableModule)
  }

  override def createIsland(name: String, cuboid: Cuboid): Island = {
    val islandModule = getModule(classOf[IslandModule]).get
    val rawIsland = new RawIsland(adapterAPI, UUID.randomUUID(), name, serverVersion, cuboid,
      cuboid.enumerateBlocks(adapter, cuboid.world.getOrElse(Bukkit.getWorlds.get(0).getName)),
      adapterAPI.entitiesGetter.enumerateEntity(cuboid))
    val island = new SpigotIsland(this, rawIsland, None)

    islandModule.addIsland(island)
    island
  }
}
