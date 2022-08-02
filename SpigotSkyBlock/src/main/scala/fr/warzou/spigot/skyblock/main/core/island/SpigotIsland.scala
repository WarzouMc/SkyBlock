package fr.warzou.spigot.skyblock.main.core.island

import fr.warzou.island.format.core.RawIsland
import fr.warzou.skyblock.adapter.api.AdapterAPI
import fr.warzou.skyblock.adapter.api.core.entity.Entity
import fr.warzou.skyblock.adapter.api.core.world.{Block, Location}
import fr.warzou.skyblock.api.common.modification
import fr.warzou.skyblock.api.common.modification.{Modification, SavedModification, Type}
import fr.warzou.skyblock.api.core.island.Island
import fr.warzou.skyblock.utils.ServerVersion
import fr.warzou.skyblock.utils.cuboid.Cuboid
import fr.warzou.spigot.skyblock.main.core.island.SpigotIsland.locationToInt

import java.io.File
import java.util.UUID
import scala.collection.immutable.Queue
import scala.collection.mutable.ListBuffer

case class SpigotIsland(rawIsland: RawIsland) extends Island {

  private var version = rawIsland.version
  private var modificationQueue = Queue.empty[Modification[_]]
  private var savedModificationList = ListBuffer.empty[SavedModification[_]]
  private val _blocks = ListBuffer.from(rawIsland.blocks)
  private val _entities = ListBuffer.from(rawIsland.entities)

  override def serverVersion: ServerVersion = version

  override def uuid: UUID = rawIsland.uuid

  override def name: String = rawIsland.name

  override def cuboid: Cuboid = rawIsland.cuboid

  override def blockCount: Int = cuboid.blockCount

  override def entityCount: Int = _entities.length

  override def entities: List[Entity] = _entities.toList

  override def addEntity(entity: Entity): Unit = ???

  override def removeEntity(entity: Entity): Unit = ???

  override def getBlockAt(location: Location): Block = _blocks(locationToInt(location, cuboid))

  override def setBlockAt(location: Location, block: Block): Unit = {
    modificationQueue = modificationQueue.enqueue(new Modification[Block] {

      private val index = locationToInt(location, cuboid)
      private val set = modification.Set(_blocks(index), block)

      override def modificationType: Type[Block] = set

      override def restore(): Unit =
        modificationQueue = modificationQueue.filterNot(_ == this)

      override def save(): SavedModification[Block] = {
        version = ServerVersion.from(rawIsland.adapterAPI.plugin)
        _blocks.update(index, block)

        restore()

        val save = SavedModification.fromModification(this)
        savedModificationList.addOne(save)
        save
      }
    })
  }

  override def blocks: List[Block] = rawIsland.blocks

  override def place(location: Location): Unit = ???

  override def modify[A](oldValue: A, newValue: A, modificationType: Type[A]): Modification[A] = ???

  override def modifications: Queue[Modification[_]] = modificationQueue

  override def savedModifications: List[SavedModification[_]] = savedModificationList.toList

  override def restoreLast(): Unit = ???

  override def restoreLast(modificationType: Type[_]): Unit = ???

  override def restoreAll(): Boolean = ???

  override def file(): File = ???

  override def save(): Boolean = ???
}

private case object SpigotIsland {

  def locationToInt(location: Location, cuboid: Cuboid): Int = xyzToInt(location.blockX, location.blockY, location.blockZ, cuboid)

  def xyzToInt(x: Int, y: Int, z: Int, cuboid: Cuboid): Int = z + y * cuboid.xSize + x * cuboid.ySize * cuboid.zSize

  def toRawIsland(adapter: AdapterAPI, island: Island): RawIsland =
    new RawIsland(adapter, island.uuid, island.name, island.serverVersion, island.cuboid, island.blocks, island.entities)
}
