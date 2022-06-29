package fr.warzou.island.format.core

import fr.warzou.island.format.reader.IslandFileReader
import fr.warzou.island.format.writer.IslandSaver
import fr.warzou.skyblock.adapter.api.AdapterAPI
import fr.warzou.skyblock.adapter.api.entity.Entity
import fr.warzou.skyblock.adapter.api.plugin.MinecraftPlugin
import fr.warzou.skyblock.adapter.api.world.Block
import fr.warzou.skyblock.utils.Version
import fr.warzou.skyblock.utils.cuboid.Cuboid
import fr.warzou.skyblock.utils.island.IslandUtils

import java.nio.file.FileAlreadyExistsException

case class RawIsland(adapterAPI: AdapterAPI, name: String, originalVersion: Version, cuboid: Cuboid, blocks: List[Block], entities: List[Entity]) {
  val plugin: MinecraftPlugin = adapterAPI.plugin
  val version: Version = Version.from(plugin)

  def save(): Unit = {
    val saver = new IslandSaver(this)
    saver.save()
  }
}


case object RawIsland {

  def create(adapter: AdapterAPI, islandName: String, cuboid: Cuboid): RawIsland = {
    checkName(adapter.plugin, islandName)
    if (cuboid.world.isEmpty)
    throw new IllegalStateException("Cannot find an unique world in cuboid !")
    val blocks = cuboid.enumerateBlocks(adapter, cuboid.world.get)
    val entities = adapter.entitiesGetter.enumerateEntity(adapter, cuboid)
    RawIsland(adapter, islandName, Version.from(adapter.plugin), cuboid, blocks, entities)
  }

  def createOrGet(adapter: AdapterAPI, islandName: String, cuboid: Cuboid): RawIsland = {
    if (IslandUtils.allIslandName(adapter.plugin).contains(islandName)) fromName(adapter, islandName)
    else create(adapter, islandName, cuboid)
  }

  def fromName(adapter: AdapterAPI, name: String): RawIsland = {
    if (!IslandUtils.allIslandName(adapter.plugin).contains(name))
      throw new IllegalArgumentException(s"Cannot found island with name '$name' !")
    val reader = new IslandFileReader(adapter, name)
    reader.read()
  }

  private def checkName(plugin: MinecraftPlugin, islandName: String): Unit = {
    val list = IslandUtils.allIslandName(plugin)
    if (list.contains(islandName))
      throw new FileAlreadyExistsException(s"Island name '$islandName' is already took !")
  }
}