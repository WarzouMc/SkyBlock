package fr.warzou.skyblock.api.common.module

import fr.warzou.skyblock.adapter.api.AdapterAPI
import fr.warzou.skyblock.api.core.island.Island
import fr.warzou.skyblock.utils.cuboid.Cuboid

abstract class ModuleHandler {

  def adapter: AdapterAPI

  def createIsland(name: String, cuboid: Cuboid): Island

}
