package fr.warzou.island.format.island.writer

import fr.warzou.island.format.core.RawIsland

import java.io.{File, FileOutputStream}
import java.util.UUID
import scala.annotation.tailrec

class IslandSaver(island: RawIsland, fileName: String) {

  private val adapter = island.adapterAPI
  private val plugin = adapter.plugin
  private val root = new File(plugin.dataFolder, "islands")

  def save(): Unit = {
    val islandFile = createFile()
    val outputStream = new FileOutputStream(islandFile)
    val writer = new IslandFileWriter(outputStream, island.version, UUID.randomUUID(), island.name, island.cuboid, island.blocks, island.entities)
    writer.write()
  }

  private def createFile(): File = {
    createIslandsRoot()
    val islandFile = new File(root, s"$fileName.island")
    if  (!islandFile.exists()) islandFile.createNewFile()
    islandFile
  }

  private def createIslandsRoot(): Unit = {
    plugin.dataFolder.mkdirs()
    root.mkdirs()
  }

}
