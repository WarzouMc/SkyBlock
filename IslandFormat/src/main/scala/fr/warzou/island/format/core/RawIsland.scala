package fr.warzou.island.format.core

import fr.warzou.island.format.reader.IslandFileReader
import fr.warzou.island.format.writer.IslandSaver
import fr.warzou.skyblock.adapter.api.AdapterAPI
import fr.warzou.skyblock.adapter.api.core.entity.Entity
import fr.warzou.skyblock.adapter.api.core.plugin.MinecraftPlugin
import fr.warzou.skyblock.adapter.api.core.world.Block
import fr.warzou.skyblock.utils.ServerVersion
import fr.warzou.skyblock.utils.cuboid.Cuboid
import fr.warzou.skyblock.utils.island.IslandUtils

import java.util.UUID

case class RawIsland(adapterAPI: AdapterAPI, uuid: UUID, name: String, originalVersion: ServerVersion, cuboid: Cuboid, blocks: List[Block], entities: List[Entity]) {
  val plugin: MinecraftPlugin = adapterAPI.plugin
  val version: ServerVersion = ServerVersion.from(plugin)

  def saveAs(fileName: String): Unit = {
    val saver = IslandSaver(this, fileName)
    saver.save()
  }
}


object RawIsland {

  def create(adapter: AdapterAPI, islandName: String, cuboid: Cuboid): RawIsland = {
    if (cuboid.world.isEmpty)
      throw new IllegalStateException("Cannot find an unique world in cuboid !")
    val blocks = cuboid.enumerateBlocks(adapter, cuboid.world.get)
    val entities = adapter.entitiesGetter.enumerateEntity(adapter, cuboid)
    RawIsland(adapter, UUID.randomUUID(), islandName, ServerVersion.from(adapter.plugin), cuboid, blocks, entities)
  }

  def createOrGet(adapter: AdapterAPI, fileName: String, cuboid: Cuboid): RawIsland = {
    if (IslandUtils.allIslandsFileName(adapter.plugin).contains(fileName)) fromFileName(adapter, fileName)
    else create(adapter, fileName, cuboid)
  }

  def fromFileName(adapter: AdapterAPI, name: String): RawIsland = {
    if (!IslandUtils.allIslandsFileName(adapter.plugin).contains(name))
      throw new IllegalArgumentException(s"Cannot found island with name '$name' !")
    val reader = IslandFileReader(adapter, name)
    reader.read
  }
}