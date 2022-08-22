package fr.warzou.skyblock.api

import fr.warzou.skyblock.adapter.api.AdapterAPI
import fr.warzou.skyblock.api.common.module.{Module, ModuleHandler}
import fr.warzou.skyblock.api.core.island.Island
import fr.warzou.skyblock.api.core.modules.island.IslandModule
import fr.warzou.skyblock.utils.cuboid.Cuboid
import fr.warzou.skyblock.utils.island.IslandMapUtils

import java.io.File
import java.util.UUID

case class SkyBlock(handler: ModuleHandler) {

  val islandsFolder: File = new File(adapter.plugin.dataFolder, "islands")

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

  def getIsland(uuid: UUID): Option[Island] = handler.getModule[IslandModule](classOf[IslandModule]).get.islandByUUID(uuid)

  def getIsland(name: String): Island = handler.getModule(classOf[IslandModule]).get.islandsByName(name).head

  private def createMainFiles(): Unit = {
    // create data folder
    val folder = adapter.plugin.dataFolder
    folder.mkdir()

    // create islands folder
    val islands = new File(folder, "islands")
    islands.mkdir()

    // create world directory
    val worlds = new File(folder, "worlds")
    val stored = new File(worlds, "stored")
    val used = new File(worlds, "used")
    stored.mkdirs()
    used.mkdir()
  }
}
