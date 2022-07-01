package fr.warzou.skyblock.api

import fr.warzou.skyblock.api.core.island.Island
import fr.warzou.skyblock.api.handler.SkyBlockHandler
import fr.warzou.skyblock.utils.cuboid.Cuboid

case class SkyBlock(handler: SkyBlockHandler) {

  def createIsland(name: String, world: String, cuboid: Cuboid): Island = createIsland(name, cuboid.applyWorld(world))

  def createIsland(name: String, cuboid: Cuboid): Island = handler.createIsland(name, cuboid)

}
