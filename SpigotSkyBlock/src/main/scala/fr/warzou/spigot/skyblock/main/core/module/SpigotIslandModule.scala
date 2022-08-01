package fr.warzou.spigot.skyblock.main.core.module

import fr.warzou.island.format.core.RawIsland
import fr.warzou.island.format.reader.IslandFileReader
import fr.warzou.skyblock.adapter.api.AdapterAPI
import fr.warzou.skyblock.api.core.modules.island.IslandModule
import fr.warzou.skyblock.utils.island.IslandUtils

case class SpigotIslandModule(private val adapter: AdapterAPI) extends IslandModule(adapter) {

  //todo
  override protected def loadIslands(): Unit = {
    val files = IslandUtils.allIslandsFileName(adapter.plugin)
    val rawIslandByFile: List[(RawIsland, String)] = files.map(file => (IslandFileReader(adapter, file).read, file)).toList
    rawIslandByFile.foreach(element => put(element._1.uuid, element._2))
  }
}
