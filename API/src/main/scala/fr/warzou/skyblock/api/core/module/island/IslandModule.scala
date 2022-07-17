package fr.warzou.skyblock.api.core.module.island

import fr.warzou.skyblock.adapter.api.AdapterAPI
import fr.warzou.skyblock.api.common.Module
import fr.warzou.skyblock.api.core.island.Island

import java.util.UUID
import scala.collection.mutable

abstract class IslandModule(private val adapter: AdapterAPI) extends Module {

  protected val linksMap = new IslandsLinksMap
  protected val islands: mutable.Map[UUID, Island] = mutable.Map[UUID, Island]()

  override def onEnable(): Unit = {
    val folder = adapter.plugin.islandFolder
    loadIslands()
  }

  override def onDisable(): Unit = ???

  def islandByUUID(uuid: UUID): Option[Island]

  def islandByFileName(fileName: String): Option[Island]

  def islandsByName(name: String): List[Island]

  protected def loadIslands(): Unit

  protected def put(uuid: UUID, fileName: String): Unit = linksMap.put(uuid, fileName)

}
