package fr.warzou.skyblock.adapter.spigot.world.world

import fr.warzou.skyblock.adapter.api.core.plugin.MinecraftPlugin
import fr.warzou.skyblock.adapter.api.core.world.world
import fr.warzou.skyblock.nms.versioning.api.NMSVersioningAPI
import fr.warzou.skyblock.nms.versioning.api.core.world.Overworld
import org.bukkit.World

import java.io.File

case class SpigotWorld(plugin: MinecraftPlugin, _world: World) extends world.World {

  private val nmsWorld = NMSVersioningAPI.getVersionAPI(plugin).getNMSWorld(_world, Overworld())

  override def name: String = _world.getName

  override def level: File = nmsWorld.level

  override def dimensions: List[SpigotDimension] = {
    val directory = nmsWorld.directory
    val _name = directory.getName
    val parent = directory.getParentFile
    val dimensionFolders = parent.listFiles((_, name) => name.startsWith(s"${_name}_")).map(file => (file,
      file.listFiles.find(_.getName.endsWith(".id")).map(_.getName.replaceAll(".id", "").toInt)))

    SpigotDimension(plugin, this, nmsWorld, isCustom = false, 0, "") ::
      dimensionFolders.map(tuple => tuple._1.getName).filter(_ != name).map(dim => {
        if (dim.endsWith("_nether")) SpigotDimension(plugin, this, nmsWorld, isCustom = false, -1, "nether")
        else if (dim.endsWith("_the_end")) SpigotDimension(plugin, this, nmsWorld, isCustom = false, 1, "the_end")
        else SpigotDimension(plugin, this, nmsWorld, isCustom = true, 1, dim.replaceFirst(_name + "_", ""))
      }
    ).toList
  }

  override def load(): Unit = dimensions.foreach(_.load())
}
