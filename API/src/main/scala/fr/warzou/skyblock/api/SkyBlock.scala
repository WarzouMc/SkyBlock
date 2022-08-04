package fr.warzou.skyblock.api

import fr.warzou.skyblock.adapter.api.AdapterAPI
import fr.warzou.skyblock.api.common.module.{Module, ModuleHandler}
import fr.warzou.skyblock.api.core.island.Island
import fr.warzou.skyblock.utils.cuboid.Cuboid

import java.io.File

case class SkyBlock(handler: ModuleHandler) {

  val islandsFolder: File = new File(adapter.plugin.dataFolder, "islands")

  //todo
  def enableAPI(): Unit = {
    createMainFiles()
    handler.enableAllModules()
  }

  def disableAPI(): Unit = {
    handler.disableAllModules()
  }

  def adapter: AdapterAPI = handler.adapter

  def createIsland(name: String, world: String, cuboid: Cuboid): Island = createIsland(name, cuboid.applyWorld(world))

  def createIsland(name: String, cuboid: Cuboid): Island = handler.createIsland(name, cuboid)

  private def createMainFiles(): Unit = {
    // create data folder
    val folder = adapter.plugin.dataFolder
    folder.mkdir()

    // create islands folder
    val islands = new File(folder, "islands")
    islands.mkdir()
  }
}
