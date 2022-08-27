package fr.warzou.skyblock.adapter.spigot.world.world

import fr.warzou.skyblock.adapter.api.core.plugin.MinecraftPlugin
import fr.warzou.skyblock.adapter.api.core.world.world.{Dimension, Region, World}
import fr.warzou.skyblock.nms.versioning.api.NMSVersioningAPI
import fr.warzou.skyblock.nms.versioning.api.world.NMSWorld
import org.bukkit.{Bukkit, WorldCreator}

import java.io.{File, FileInputStream}

case class SpigotDimension(plugin: MinecraftPlugin, world: World, nmsWorld: NMSWorld, isCustom: Boolean, id: Int, name: String)
  extends Dimension {

  override def regionCount: Int = regions().length

  override def regions(): List[Region] = regionExplorer(nmsWorld.regionFolder)

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
    new Region(x, y, bytes) {
      //todo change notCompress to compress ...
      override def compressMCA(): Array[Byte] = NMSVersioningAPI.getVersionAPI(plugin).getNBTTools.mcaCompresser(bytes).notCompress
    }
  }

  private[world] def load(): Unit = {
    //todo create .id file (think about another solution)
    Bukkit.createWorld(WorldCreator.name(s"${world.name}_$name"))
  }
}
