package fr.warzou.spigot.skyblock.main.core.module

import fr.warzou.island.format.island.reader.IslandFileReader
import fr.warzou.skyblock.adapter.api.AdapterAPI
import fr.warzou.skyblock.api.core.module.island.IslandModule
import fr.warzou.skyblock.utils.island.IslandUtils

class SpigotIslandModule(adapter: AdapterAPI) extends IslandModule(adapter) {

  override protected def loadIslands(): Unit = {
    val files = IslandUtils.allIslandsName(adapter.plugin)
    files.map(file => (new IslandFileReader(adapter, file).read, file)).foreach(value => linksMap.put(value._1.uuid, value._2))
  }
}
