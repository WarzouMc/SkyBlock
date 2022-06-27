package fr.warzou.spigot.skyblock.main

import fr.warzou.island.format.writer.IslandFileWriter
import fr.warzou.skyblock.adapter.api.AdapterAPI
import fr.warzou.skyblock.utils.cuboid.Cuboid
import fr.warzou.skyblock.utils.server.Spigot
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

class SkyBlock extends JavaPlugin {

  override def onEnable(): Unit = {
    val adapter = AdapterAPI.createAdapter(Spigot(), this)
    val loc0 = adapter.createLocation(0, 100, 0)
    val loc1 = adapter.createLocation(10, 107, 10)
    val rawIsland = new IslandFileWriter(adapter, "OWO", Bukkit.getWorlds.get(0).getName, new Cuboid(loc0, loc1)).writeIsland()
  }
}
