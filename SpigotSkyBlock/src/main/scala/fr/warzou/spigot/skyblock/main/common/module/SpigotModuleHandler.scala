package fr.warzou.spigot.skyblock.main.common.module

import fr.warzou.skyblock.adapter.api.AdapterAPI
import fr.warzou.skyblock.api.common.ModuleHandler
import fr.warzou.skyblock.api.core.island.Island
import fr.warzou.skyblock.utils.cuboid.Cuboid
import fr.warzou.skyblock.utils.server
import org.bukkit.plugin.Plugin

class SpigotModuleHandler(plugin: Plugin) extends ModuleHandler {

  private val adapterAPI = AdapterAPI.createAdapter(server.Spigot(), plugin)

  override def adapter: AdapterAPI = adapterAPI

  override def createIsland(name: String, cuboid: Cuboid): Island = ???
}
