package fr.warzou.skyblock.api

import fr.warzou.skyblock.adapter.api.AdapterAPI
import fr.warzou.skyblock.api.common.module.ModuleHandler
import fr.warzou.skyblock.api.core.island.Island
import fr.warzou.skyblock.api.core.modules.island.IslandModule
import fr.warzou.skyblock.api.managers.IslandManager
import fr.warzou.skyblock.utils.cuboid.Cuboid

import java.io.File
import java.util.UUID

//todo split into some sub managing classes
case class SkyBlockAPI(handler: ModuleHandler) {

  if (SkyBlockAPI.isInitialized) throw new IllegalStateException("Already initialized api !")
  SkyBlockAPI.isInitialized = true
  SkyBlockAPI.skyBlockAPI = this

  val islandsFolder: File = new File(adapter.plugin.dataFolder, "islands")
  private var _islandManager: IslandManager = _

  def enableAPI(): Unit = {
    createMainFiles()
    handler.enableAllModules()
    initIslandManager()
  }

  def disableAPI(): Unit = {
    handler.disableAllModules()
  }

  def islandManager: IslandManager = _islandManager

  def adapter: AdapterAPI = handler.adapter

  def createIsland(name: String, world: String, cuboid: Cuboid): Island = createIsland(name, cuboid.applyWorld(world))

  def createIsland(name: String, cuboid: Cuboid): Island = handler.createIsland(name, cuboid)

  def islandList(): List[Island] = handler.getModule[IslandModule](classOf[IslandModule]).get.allIsland()

  def getIsland(uuid: UUID): Option[Island] = handler.getModule[IslandModule](classOf[IslandModule]).get.islandByUUID(uuid)

  def getIsland(name: String): Island = handler.getModule(classOf[IslandModule]).get.islandsByName(name).head

  private def initIslandManager(): Unit = {
    _islandManager = new IslandManager(handler.getModule[IslandModule](classOf[IslandModule])
      .getOrElse(throw new RuntimeException("No IslandModule is enable !")))
  }

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

object SkyBlockAPI {
  private var isInitialized = false
  private var skyBlockAPI: SkyBlockAPI = _

  def adapaterAPI: AdapterAPI = skyBlockAPI.adapter

  def islandManager: IslandManager = skyBlockAPI.islandManager
}
