package fr.warzou.skyblock.api.core.island

import fr.warzou.skyblock.adapter.api.core.entity.Entity
import fr.warzou.skyblock.adapter.api.core.world.{Block, Location}
import fr.warzou.skyblock.api.core.modifiable.Modifiable
import fr.warzou.skyblock.api.core.saveable.Saveable
import fr.warzou.skyblock.utils.ServerVersion
import fr.warzou.skyblock.utils.cuboid.Cuboid

import java.util.UUID

trait Island extends Saveable with Modifiable {

  def serverVersion: ServerVersion

  def uuid: UUID

  def name: String

  def cuboid: Cuboid

  def blockCount: Int

  def entityCount: Int

  def entities: List[Entity]

  def addEntity(entity: Entity): Unit

  def removeEntity(entity: Entity): Unit

  def getBlockAt(location: Location): Block

  def setBlockAt(location: Location, block: Block): Unit

  def blocks: List[Block]

  def place(location: Location): Unit

  override def equals(obj: Any): Boolean = {
    if (obj == null || !obj.isInstanceOf[Island]) return false
    obj.asInstanceOf[Island].uuid == uuid
  }

}
