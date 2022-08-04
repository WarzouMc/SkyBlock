package fr.warzou.skyblock.api.core.modules.island

import fr.warzou.island.format.core.RawIsland
import fr.warzou.skyblock.adapter.api.AdapterAPI
import fr.warzou.skyblock.api.common.module.Module
import fr.warzou.skyblock.api.core.island.Island
import fr.warzou.skyblock.utils.collection.map.mutable.BijectiveMap

import java.io.File
import java.util.UUID

abstract class IslandModule(private val adapter: AdapterAPI) extends Module {

  protected val linksMap = new IslandsLinksMap
  protected val islands: BijectiveMap[UUID, Island] = BijectiveMap.createHashBijectiveMap()
  private val root = new File(adapter.plugin.dataFolder, "islands")

  override def enable(): Unit = {
    val folder = adapter.plugin.islandFolder
    loadIslands()
  }

  override def disable(): Unit = {
    islands.values.foreach(_.save())
  }

  def addIsland(island: Island): Unit = {
    if (islands.containsKey(island.uuid)) throw new IllegalArgumentException("Already register island !")
    islands.put(island.uuid, island)
    put(island.uuid, island.fileName)
  }

  def setFile(target: Island, newFileName: String): Unit = linksMap.map.setValue(target.uuid, newFileName)

  def islandByUUID(uuid: UUID): Option[Island] = islands.fromKey(uuid)

  def islandByFileName(fileName: String): Option[Island] = linksMap.getUUID(fileName) match {
    case Some(uuid) => islands.fromKey(uuid)
    case None => None
  }

  def islandsByName(name: String): List[Island] = islands.values.filter(_.name == name).toList

  protected def loadIslands(): Unit

  protected def put(uuid: UUID, fileName: String): Unit = linksMap.put(uuid, fileName)

  private def toRawIsland(island: Island): RawIsland =
    RawIsland(adapter, island.uuid, island.name, island.serverVersion, island.cuboid, island.blocks, island.entities)

  protected def asIslandFile(fileName: String): File = new File(root, s"$fileName.island")
}
