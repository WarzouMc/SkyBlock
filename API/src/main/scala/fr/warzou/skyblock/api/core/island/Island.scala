package fr.warzou.skyblock.api.core.island

import fr.warzou.skyblock.adapter.api.entity.Entity
import fr.warzou.skyblock.adapter.api.world.{Block, Location}
import fr.warzou.skyblock.utils.cuboid.Cuboid

trait Island {

  def name: String

  def cuboid: Cuboid

  def blockCount: Int

  def entityCount: Int

  def getEntities: Array[Entity]

  def getBlockAt(location: Location): Block

  def setBlockAt(location: Location, block: Block): Unit

  def save(): Unit

  def place(location: Location): Unit

}
