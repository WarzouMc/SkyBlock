package fr.warzou.island.format.writer

import fr.warzou.island.format.core.RawIsland
import fr.warzou.island.format.reader.IslandFileReader
import fr.warzou.skyblock.adapter.api.AdapterAPI
import fr.warzou.skyblock.utils.Version
import fr.warzou.skyblock.utils.cuboid.Cuboid

import java.io.{File, FileOutputStream}

class IslandFileWriter(adapter: AdapterAPI, name: String, worldName: String, cuboid: Cuboid) {

  private val plugin = adapter.getPlugin
  private val root = new File(plugin.getDataFolder, "islands")

  def writeIsland(): RawIsland = {
    val islandFile = createFile()
    val outputStream = new FileOutputStream(islandFile)
    val writer = new Writer(outputStream, Version.from(plugin), cuboid, cuboid.enumerateBlocks(adapter, worldName), adapter.entitiesGetter.enumerateEntity(adapter, cuboid))
    writer.write()
    new IslandFileReader(adapter, name).read()
  }

  private def createFile(): File = {
    createIslandsRoot()
    val islandFile = new File(root, s"$name.island")
    if  (!islandFile.exists()) islandFile.createNewFile()
    islandFile
  }

  private def createIslandsRoot(): Unit = {
    plugin.getDataFolder.mkdirs()
    root.mkdirs()
  }

}
