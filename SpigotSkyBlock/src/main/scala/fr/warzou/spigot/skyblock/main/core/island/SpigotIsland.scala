package fr.warzou.spigot.skyblock.main.core.island

import fr.warzou.island.format.core.RawIsland
import fr.warzou.skyblock.adapter.api.core.entity.Entity
import fr.warzou.skyblock.adapter.api.core.world.{Block, Location}
import fr.warzou.skyblock.api.common.modification
import fr.warzou.skyblock.api.common.modification.{Modification, SavedModification, Type}
import fr.warzou.skyblock.api.core.island.Island
import fr.warzou.skyblock.utils.ServerVersion
import fr.warzou.skyblock.utils.cuboid.Cuboid

import java.io.File
import java.util.UUID
import scala.collection.immutable.Queue

case class SpigotIsland(rawIsland: RawIsland) extends Island {

  private var modificationQueue = Queue.empty[Modification[_]]

  override def serverVersion: ServerVersion = rawIsland.version

  override def uuid: UUID = rawIsland.uuid

  override def name: String = rawIsland.name

  override def cuboid: Cuboid = rawIsland.cuboid

  override def blockCount: Int = cuboid.blockCount

  override def entityCount: Int = entities.length

  override def entities: List[Entity] = rawIsland.entities

  override def addEntity(entity: Entity): Unit = ???

  override def removeEntity(entity: Entity): Unit = ???

  override def getBlockAt(location: Location): Block = xyzBlock(location.blockX, location.blockY, location.blockZ)

  override def setBlockAt(location: Location, block: Block): Unit = {

  }

  override def blocks: List[Block] = ???

  override def place(location: Location): Unit = ???

  override def modify[A](oldValue: A, newValue: A, modificationType: Type[A]): Modification[A] = ???

  override def modifications: Queue[Modification[_]] = modificationQueue

  override def restoreLast(): Unit = ???

  override def restoreLast(modificationType: Type[_]): Unit = ???

  override def restoreAll(): Boolean = ???

  override def file(): File = ???

  override def save(): Boolean = ???

  private def xyzBlock(x: Int, y: Int, z: Int): Block = {
    val _cuboid = cuboid
    val _blocks = blocks
    _blocks(z + y * _cuboid.xSize + x * _cuboid.ySize * _cuboid.zSize)
  }
}
