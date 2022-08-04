package fr.warzou.spigot.skyblock.main.common.module

import fr.warzou.island.format.core.RawIsland
import fr.warzou.skyblock.adapter.api.AdapterAPI
import fr.warzou.skyblock.api.common.module.{Module, ModuleHandler}
import fr.warzou.skyblock.api.core.island.Island
import fr.warzou.skyblock.api.core.modules.island.IslandModule
import fr.warzou.skyblock.utils.cuboid.Cuboid
import fr.warzou.skyblock.utils.{ServerVersion, server}
import fr.warzou.spigot.skyblock.main.core.island.SpigotIsland
import fr.warzou.spigot.skyblock.main.core.module.SpigotIslandModule
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
      adapterAPI.entitiesGetter.enumerateEntity(adapter, cuboid))
    val island = new SpigotIsland(this, rawIsland, None)

    islandModule.addIsland(island)
    island
  }

  def getModule[A <: Module](clazz: Class[A]): Option[A] = _modules.find(_.getClass.isAssignableFrom(clazz)).map(_.asInstanceOf[A])
}
