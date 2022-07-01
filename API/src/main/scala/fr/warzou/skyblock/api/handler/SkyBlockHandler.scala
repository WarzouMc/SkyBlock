package fr.warzou.skyblock.api.handler

import fr.warzou.skyblock.api.core.island.Island
import fr.warzou.skyblock.utils.cuboid.Cuboid

abstract class SkyBlockHandler {

  def createIsland(name: String, cuboid: Cuboid): Island

}
