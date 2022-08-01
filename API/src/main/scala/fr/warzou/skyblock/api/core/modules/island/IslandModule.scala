package fr.warzou.skyblock.api.core.modules.island

import fr.warzou.island.format.core.RawIsland
import fr.warzou.skyblock.adapter.api.AdapterAPI
import fr.warzou.skyblock.api.common.module.Module
import fr.warzou.skyblock.api.core.island.Island
import fr.warzou.skyblock.utils.collection.map.mutable.BijectiveMap

import java.util.UUID

abstract case class IslandModule(private val adapter: AdapterAPI) extends Module {

  protected val linksMap = new IslandsLinksMap
  protected val islands: BijectiveMap[UUID, Island] = BijectiveMap.createHashBijectiveMap()

  override def onEnable(): Unit = {
    val folder = adapter.plugin.islandFolder
    loadIslands()
  }

  override def onDisable(): Unit = {
    islands.values.foreach(_.save())
  }

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
}
