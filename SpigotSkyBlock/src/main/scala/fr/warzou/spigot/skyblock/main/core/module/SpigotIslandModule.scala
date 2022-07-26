package fr.warzou.spigot.skyblock.main.core.module

import fr.warzou.island.format.core.RawIsland
import fr.warzou.island.format.reader.IslandFileReader
import fr.warzou.skyblock.adapter.api.AdapterAPI
import fr.warzou.skyblock.adapter.spigot.entity.SpigotEntity
import fr.warzou.skyblock.api.core.module.island.IslandModule
import fr.warzou.skyblock.utils.island.IslandUtils

class SpigotIslandModule(adapter: AdapterAPI) extends IslandModule(adapter) {

  override protected def loadIslands(): Unit = {
    val files = IslandUtils.allIslandsName(adapter.plugin)
    val rawIslandByFile: List[(RawIsland, String)] = files.map(file => (new IslandFileReader(adapter, file).read, file)).toList
    rawIslandByFile.foreach(element => put(element._1.uuid, element._2))
    SpigotEntity.toCustom(null)
  }
}
