package fr.warzou.skyblock.spigot.main

import fr.warzou.skyblock.adapter.spigot.world.SpigotLocation
import fr.warzou.skyblock.spigot.main.common.module.SpigotModuleHandler
import fr.warzou.skyblock.utils.cuboid.Cuboid
import org.bukkit.Bukkit
import org.bukkit.command.{Command, CommandSender}
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

class SpigotSkyBlock extends JavaPlugin {

  private val handler = SpigotModuleHandler(this)
  private val api = fr.warzou.skyblock.api.SkyBlock(handler)

  override def onEnable(): Unit = {
    getCommand("place").setExecutor(this)
    api.enableAPI()

    val loc0 = api.adapter.createLocation(Bukkit.getWorlds.get(0).getName, 0, 100, 20)
    val loc1 = api.adapter.createLocation(Bukkit.getWorlds.get(0).getName, 10, 106, 30)
    val cuboid = Cuboid(loc0, loc1)
    val island = api.createIsland("An_island", cuboid)

    island.withFileName("faut_un_nom")
    island.save(true)

    //island.place(api.adapter.createLocation(20, 110, 20))
  }

  override def onCommand(sender: CommandSender, command: Command, label: String, args: Array[String]): Boolean = {
    if (command.getName == "place") {
      val islandName = args(0)
      val location = SpigotLocation.wrap(sender.asInstanceOf[Player].getLocation()).appendXYZ(0, -1, 0)
      val island = api.getIsland(islandName)
      island.place(location)
      return true
    }
    false
  }
}
