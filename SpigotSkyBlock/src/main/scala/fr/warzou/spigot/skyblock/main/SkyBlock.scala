package fr.warzou.spigot.skyblock.main

import fr.il_totore.spigotmetadata.api.SpigotMetadataAPI
import fr.il_totore.spigotmetadata.api.nbt.{NBTInputStream, NBTTagType}
import fr.warzou.island.format.core.RawIsland
import fr.warzou.island.format.writer.IslandFileWriter
import fr.warzou.skyblock.adapter.api.AdapterAPI
import fr.warzou.skyblock.utils.cuboid.Cuboid
import fr.warzou.skyblock.utils.server.Spigot
import org.bukkit.block.Block
import org.bukkit.{Bukkit, Location, Material, NamespacedKey}
import org.bukkit.plugin.java.JavaPlugin

import java.io.ByteArrayInputStream

class SkyBlock extends JavaPlugin {

  override def onEnable(): Unit = {
    val adapter = AdapterAPI.createAdapter(Spigot(), this)
    val loc0 = adapter.createLocation(0, 100, 0)
    val loc1 = adapter.createLocation(10, 107, 10)
    val rawIsland = new IslandFileWriter(adapter, "OWO", Bukkit.getWorlds.get(0).getName, new Cuboid(loc0, loc1)).writeIsland()
    place(rawIsland)
  }

  def place(island: RawIsland): Unit = {
    val x = 0
    val y = 100
    val z = 20
    (0 to 10).foreach(_x => {
      (0 to 7).foreach(_y => {
        (0 to 10).foreach(_z => {
          val loc = new Location(Bukkit.getWorlds.get(0), _x + x, _y + y, _z + z)
          val block = island.blocks(_z + _y * 11 + _x * 11 * 8)
          Bukkit.getWorlds.get(0).getBlockAt(loc).setType(Material.valueOf(block.name.split(":")(1).toUpperCase))
          Bukkit.getWorlds.get(0).getBlockAt(loc).setData(block.data.toByte)
          if (block.isBlockEntity) {
            println(block.nbt.get.mkString("Array(", ", ", ")"))
            val byteArrayInputStream = new ByteArrayInputStream(block.nbt.get)
            val nbtManager = SpigotMetadataAPI.getAPI.getNBTManager
            val inputStream = new NBTInputStream(nbtManager, byteArrayInputStream)
            nbtManager.setNBTTag(Bukkit.getWorlds.get(0).getBlockAt(loc), inputStream.readTag(NBTTagType.COMPOUND))
          }
        })
      })
    })
  }
}
