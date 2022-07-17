package fr.warzou.spigot.skyblock.main

import fr.il_totore.spigotmetadata.api.SpigotMetadataAPI
import fr.il_totore.spigotmetadata.api.nbt.{NBTInputStream, NBTTagCompound}
import fr.warzou.island.format.core.RawIsland
import fr.warzou.skyblock.adapter.api.AdapterAPI
import fr.warzou.skyblock.api.SkyBlock
import fr.warzou.skyblock.utils.cuboid.Cuboid
import fr.warzou.skyblock.utils.server.Spigot
import fr.warzou.spigot.skyblock.main.common.module.SpigotModuleHandler
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.{Bukkit, Location, Material}

import java.io.ByteArrayInputStream

class SkyBlock extends JavaPlugin {

  private val handler = new SpigotModuleHandler(this)
  private val api = fr.warzou.skyblock.api.SkyBlock(handler)

  override def onEnable(): Unit = {
    val loc0 = api.adapter.createLocation(Bukkit.getWorlds.get(0).getName, 0, 100, 0)
    val loc1 = api.adapter.createLocation(Bukkit.getWorlds.get(0).getName, 10, 107, 10)
    val cuboid = Cuboid(loc0, loc1)
    val island = RawIsland.createOrGet(api.adapter, "faut_un_nom", cuboid)
    new BukkitRunnable {
      override def run(): Unit = {
        place(island)
      }
    }.runTaskLater(this, 20 * 3)
  }

  def place(island: RawIsland): Unit = {
    val x = 0
    val y = 100
    val z = 20
    (0 to 10).foreach(_x => {
      (0 to 7).foreach(_y => {
        (0 to 10).foreach(_z => {
          val location = new Location(Bukkit.getWorlds.get(0), _x + x, _y + y, _z + z)
          val block = island.blocks(_z + _y * 11 + _x * 11 * 8)
          Bukkit.getWorlds.get(0).getBlockAt(location).setType(Material.valueOf(block.name.split(":")(1).toUpperCase))
          Bukkit.getWorlds.get(0).getBlockAt(location).setData(block.data.toByte)
          if (block.isBlockEntity) {
            val byteArrayInputStream = new ByteArrayInputStream(block.nbt.get)
            val nbtManager = SpigotMetadataAPI.getAPI.getNBTManager
            val inputStream = new NBTInputStream(nbtManager, byteArrayInputStream)
            val compound = inputStream.readTag().asInstanceOf[NBTTagCompound]
            compound.setInt("x", location.getBlockX)
            compound.setInt("y", location.getBlockY)
            compound.setInt("z", location.getBlockZ)
            nbtManager.setNBTTag(Bukkit.getWorlds.get(0).getBlockAt(location), compound)
          }
        })
      })
    })
  }
}
