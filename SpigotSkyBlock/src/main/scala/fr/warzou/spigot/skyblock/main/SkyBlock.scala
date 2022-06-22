package fr.warzou.spigot.skyblock.main

import fr.warzou.island.format.core.common.cuboid.Cuboid
import fr.warzou.island.format.writer.file.IslandFileWriter
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.{Bukkit, Location}

class SkyBlock extends JavaPlugin {

  override def onEnable(): Unit = {
    val loc0 = new Location(Bukkit.getWorlds.get(0), 0, 100, 0)
    val loc1 = new Location(Bukkit.getWorlds.get(0), 10, 107, 10)
    val rawIsland = new IslandFileWriter(this, "OWO", new Cuboid(loc0, loc1)).writeIsland()
  }
}
