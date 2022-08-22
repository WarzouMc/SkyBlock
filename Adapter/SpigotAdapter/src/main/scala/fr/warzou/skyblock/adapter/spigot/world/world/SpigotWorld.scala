package fr.warzou.skyblock.adapter.spigot.world.world

import fr.warzou.skyblock.adapter.api.core.plugin.MinecraftPlugin
import fr.warzou.skyblock.adapter.api.core.world.world
import fr.warzou.skyblock.adapter.api.core.world.world.Region
import fr.warzou.skyblock.nms.versioning.api.NMSVersioningAPI
import org.bukkit.World
import org.bukkit.World.Environment

import java.io.{ByteArrayInputStream, File, FileFilter, FileInputStream}

case class SpigotWorld(plugin: MinecraftPlugin, _id: Int, _world: World) extends world.World {

  override def id: Int = _id

  override def worldEnvironmentId: Int = _world.getEnvironment match {
    case Environment.NORMAL => 0
    case Environment.NETHER => -1
    case Environment.THE_END => 1
  }

  override def regions: List[Region] = {
    val directory = NMSVersioningAPI.getVersionAPI(plugin).getNMSWorld(_world).directory
    regionExplorer(new File(directory,
      if (worldEnvironmentId == 0) "region"
      else s"DIM$worldEnvironmentId${File.pathSeparator}region")
    )
  }

  private def regionExplorer(regionDirectory: File): List[Region] =
    recRegionExplorer(regionDirectory.listFiles((_, name) => name.endsWith(".mca")).toList)

  private def recRegionExplorer(files: List[File]): List[Region] = files match {
    case Nil => Nil
    case file :: tail => mcaToRegion(file) :: recRegionExplorer(tail)
  }

  private def mcaToRegion(file: File): Region = {
    val nameSplit = file.getName.split('.')
    val x = Integer.parseInt(nameSplit(1))
    val y = Integer.parseInt(nameSplit(2))
    val inputStream = new FileInputStream(file)
    val bytes = inputStream.readAllBytes()
    Region(x, y, bytes)
  }
}
