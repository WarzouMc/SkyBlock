package fr.warzou.skyblock.utils.island

import fr.warzou.skyblock.adapter.api.plugin.MinecraftPlugin

import java.io.File

case object IslandUtils {

  def storageFolder(plugin: MinecraftPlugin): File = new File(plugin.getDataFolder, "islands")

  def allIslandName(plugin: MinecraftPlugin): Array[String] = {
    val folder = storageFolder(plugin)
    folder.listFiles().filter(_.isFile).map(_.getName).filter(_.split("\\.").length == 2)
      .filter(_.split("\\.")(1) == "island")
  }

  def allIslandFile(plugin: MinecraftPlugin): Array[File] = {
    val folder = storageFolder(plugin)
    allIslandName(plugin).map(new File(folder, _))
  }
}
