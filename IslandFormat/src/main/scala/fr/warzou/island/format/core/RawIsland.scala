package fr.warzou.island.format.core

import fr.warzou.island.format.core.io.{Reader, Writer}
import fr.warzou.island.format.reader.IslandFileReader
import fr.warzou.island.format.writer.IslandFileWriter
import fr.warzou.skyblock.adapter.api.AdapterAPI
import fr.warzou.skyblock.adapter.api.core.entity.Entity
import fr.warzou.skyblock.adapter.api.core.plugin.MinecraftPlugin
import fr.warzou.skyblock.adapter.api.core.world.Block
import fr.warzou.skyblock.utils.ServerVersion
import fr.warzou.skyblock.utils.cuboid.Cuboid
import fr.warzou.skyblock.utils.island.IslandUtils

import java.io.{File, FileOutputStream}
import java.util.UUID

case class RawIsland(adapterAPI: AdapterAPI, uuid: UUID, name: String, originalVersion: ServerVersion, cuboid: Cuboid,
                     blocks: List[Block], entities: List[Entity]) {

  val plugin: MinecraftPlugin = adapterAPI.plugin
  val version: ServerVersion = ServerVersion.from(plugin)
  private val root = new File(plugin.dataFolder, "islands")

  def saveAs(fileName: String): Unit = {
    val file = createFile(fileName)
    val writer = IslandFileWriter(new FileOutputStream(file), plugin, version, uuid, name, cuboid, blocks, entities)
    plugin.logger.io(s"Save island '$name' with uuid '$uuid' as file '$fileName'.")
    save(writer)
  }

  def save(writer: Writer): Unit = writer.write()

  private def createFile(fileName: String): File = {
    createIslandsRoot()
    val islandFile = new File(root, s"$fileName.island")
    if (!islandFile.exists()) islandFile.createNewFile()
    islandFile
  }

  private def createIslandsRoot(): Unit = {
    plugin.dataFolder.mkdirs()
    root.mkdirs()
  }
}


object RawIsland {

  def read(reader: Reader): RawIsland = reader.read

  def create(adapter: AdapterAPI, islandName: String, cuboid: Cuboid): RawIsland = {
    if (cuboid.world.isEmpty)
      throw new IllegalStateException("Cannot find an unique world in cuboid !")
    val blocks = cuboid.enumerateBlocks(adapter, cuboid.world.get)
    val entities = adapter.entitiesGetter().enumerateEntity(adapter, cuboid)
    RawIsland(adapter, UUID.randomUUID(), islandName, ServerVersion.from(adapter.plugin), cuboid, blocks, entities)
  }

  def createOrGet(adapter: AdapterAPI, fileName: String, cuboid: Cuboid): RawIsland = {
    if (IslandUtils.allIslandsFileName(adapter.plugin).contains(fileName)) fromFileName(adapter, fileName)
    else create(adapter, fileName, cuboid)
  }

  def fromFileName(adapter: AdapterAPI, fileName: String): RawIsland = {
    if (!IslandUtils.allIslandsFileName(adapter.plugin).contains(fileName))
      throw new IllegalArgumentException(s"Cannot found island with name '$fileName' !")
    read(IslandFileReader(adapter, fileName))
  }
}