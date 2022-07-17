package fr.warzou.skyblock.api.core.island

import fr.warzou.skyblock.adapter.api.entity.Entity
import fr.warzou.skyblock.adapter.api.world.{Block, Location}
import fr.warzou.skyblock.utils.cuboid.Cuboid

import java.util.UUID

trait Island {

  def uuid: UUID

  def name: String

  def cuboid: Cuboid

  def blockCount: Int

  def entityCount: Int

  def getEntities: Array[Entity]

  def getBlockAt(location: Location): Block

  def setBlockAt(location: Location, block: Block): Unit

  def blocks: List[Block]

  def place(location: Location): Unit

  def save(): Unit

  override def equals(obj: Any): Boolean = {
    if (obj == null || !obj.isInstanceOf[Island]) return false
    obj.asInstanceOf[Island].uuid == uuid
  }

}
