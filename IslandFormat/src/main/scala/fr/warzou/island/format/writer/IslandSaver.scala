package fr.warzou.island.format.writer

import fr.warzou.island.format.core.RawIsland

import java.io.{File, FileOutputStream}

class IslandSaver(island: RawIsland) {

  private val adapter = island.adapterAPI
  private val plugin = adapter.plugin
  private val root = new File(plugin.getDataFolder, "islands")

  def save(): Unit = {
    val islandFile = createFile()
    val outputStream = new FileOutputStream(islandFile)
    val writer = new Writer(outputStream, island.version, island.cuboid, island.blocks, island.entities)
    writer.write()
  }

  private def createFile(): File = {
    createIslandsRoot()
    val islandFile = new File(root, s"${island.name}.island")
    if  (!islandFile.exists()) islandFile.createNewFile()
    islandFile
  }

  private def createIslandsRoot(): Unit = {
    plugin.getDataFolder.mkdirs()
    root.mkdirs()
  }

}
