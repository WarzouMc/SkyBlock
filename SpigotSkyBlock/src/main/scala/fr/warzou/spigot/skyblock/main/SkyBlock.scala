package fr.warzou.spigot.skyblock.main

import fr.warzou.island.format.core.Island
import fr.warzou.skyblock.adapter.api.AdapterAPI
import fr.warzou.skyblock.utils.cuboid.Cuboid
import fr.warzou.skyblock.utils.server.Spigot
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.{Bukkit, Location, Material}

class SkyBlock extends JavaPlugin {

  override def onEnable(): Unit = {
    val adapter = AdapterAPI.createAdapter(Spigot(), this)
    val loc0 = adapter.createLocation(0, 100, 0)
    val loc1 = adapter.createLocation(10, 107, 10)
    val cuboid = Cuboid(loc0, loc1)
    val island = Island.createOrGet(adapter, "faut_un_nom", cuboid)
    place(island)
  }

  def place(island: Island): Unit = {
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
          /*if (block.isBlockEntity) {
            println("after read -> " + block.nbt.get.mkString("Array(", ", ", ")") + " length : " + block.nbt.get.length)
            val byteArrayInputStream = new ByteArrayInputStream(block.nbt.get)
            val nbtManager = SpigotMetadataAPI.getAPI.getNBTManager
            val inputStream = new NBTInputStream(nbtManager, byteArrayInputStream)
            nbtManager.setNBTTag(Bukkit.getWorlds.get(0).getBlockAt(location), inputStream.readTag(NBTTagType.COMPOUND))
          }*/
        })
      })
    })
  }
}
