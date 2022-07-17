package fr.warzou.skyblock.api.core.module.island

import fr.warzou.skyblock.adapter.api.AdapterAPI
import fr.warzou.skyblock.api.common.Module

abstract class IslandModule(private val adapter: AdapterAPI) extends Module {

  protected val linksMap = new IslandsLinksMap

  override def onEnable(): Unit = {
    val folder = adapter.plugin.islandFolder
    loadIslands()
  }

  override def onDisable(): Unit = ???

  protected def loadIslands(): Unit

}
