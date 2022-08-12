package fr.warzou.skyblock.spigot.main.core.island

import fr.warzou.island.format.core.RawIsland
import fr.warzou.skyblock.adapter.api.AdapterAPI
import fr.warzou.skyblock.adapter.api.core.entity.Entity
import fr.warzou.skyblock.adapter.api.core.world.{Block, Location}
import fr.warzou.skyblock.adapter.spigot.entity.SpigotEntity
import fr.warzou.skyblock.adapter.spigot.world.SpigotLocation
import fr.warzou.skyblock.api.common.module.ModuleHandler
import fr.warzou.skyblock.api.core.island.Island
import fr.warzou.skyblock.api.core.modules.island.IslandModule
import fr.warzou.skyblock.nms.versioning.api.NMSVersioningAPI
import fr.warzou.skyblock.nms.versioning.api.block.BlockWrap
import fr.warzou.skyblock.nms.versioning.api.entity.EntityWrap
import fr.warzou.skyblock.spigot.main.core.island.SpigotIsland.{locationToInt, xyzToInt}
import fr.warzou.skyblock.utils.ServerVersion
import fr.warzou.skyblock.utils.cuboid.Cuboid
import org.bukkit
import org.bukkit.block.Container
import org.bukkit.entity.EntityType
import org.bukkit.inventory.ItemStack
import org.bukkit.{Bukkit, Material, NamespacedKey}

import java.io.File
import java.util.UUID
import scala.collection.mutable.ListBuffer

case class SpigotIsland(private val moduleHandler: ModuleHandler, private val rawIsland: RawIsland,
                        private val optionFile: Option[File]) extends Island {

  private val root = new File(rawIsland.adapterAPI.plugin.dataFolder, "islands")
  private val currentVersion = ServerVersion.from(rawIsland.adapterAPI.plugin)
  private var version = rawIsland.version
  private val _blocks = ListBuffer.from(rawIsland.blocks)
  private val _entities = ListBuffer.from(rawIsland.entities)
  private var _file = optionFile.getOrElse(asFile(rawIsland.name))

  override def serverVersion: ServerVersion = version

  override def uuid: UUID = rawIsland.uuid

  override def name: String = rawIsland.name

  override def cuboid: Cuboid = rawIsland.cuboid

  override def blockCount: Int = cuboid.blockCount

  override def entityCount: Int = _entities.length

  override def entities: List[Entity] = _entities.toList

  override def addEntity(entity: Entity): Unit = {
    updateVersion()
    _entities.addOne(entity)
  }

  override def removeEntity(entity: Entity): Unit = _entities.remove(_entities.indexOf(entity))

  override def getBlockAt(location: Location): Block = _blocks(locationToInt(location, cuboid))

  override def setBlockAt(location: Location, block: Block): Unit = {
    updateVersion()
    _blocks.update(locationToInt(location, cuboid), block)
  }

  override def blocks: List[Block] = _blocks.toList

  override def place(location: Location): Unit = {
    clearCuboidAt(location)
    (0 until cuboid.xSize).foreach(x => {
      (0 until cuboid.ySize).foreach(y => {
        (0 until cuboid.zSize).foreach(z => {
          val index = xyzToInt(x, y, z, cuboid)
          val blockLocation = new bukkit.Location(Bukkit.getWorld(location.world.getOrElse(Bukkit.getWorlds.get(0).getName)),
            location.blockX + x, location.blockY + y, location.blockZ + z)
          placeBlock(index, blockLocation)
        })
      })
    })
    entities.foreach(placeEntity(_, location))
  }

  override def withFileName(name: String, deleteOld: Boolean): Unit = {
    val islandModule = moduleHandler.getModule[IslandModule](classOf[IslandModule]).get
    if (islandModule.islandByFileName(name).isDefined)
      throw new IllegalArgumentException(s"Already took file name for $name !")

    if (deleteOld) _file.delete()
    _file = asFile(name)
    islandModule.setFile(this, name)
  }

  override def fileName: String = _file.getName

  override def file: File = _file

  override def save(force: Boolean): Boolean = {
    if (!force && _blocks.equals(rawIsland.blocks) && _entities.equals(rawIsland.entities) && _file.exists()) return false

    val newRawIsland = RawIsland(rawIsland.adapterAPI, uuid, name, serverVersion, cuboid, blocks, entities)
    newRawIsland.saveAs(_file.getName.replace(".island", ""))
    true
  }

  private def clearCuboidAt(location: Location): Unit = {
    (0 until cuboid.xSize).foreach(x => {
      (0 until cuboid.ySize).foreach(y => {
        (0 until cuboid.zSize).foreach(z => {
          val blockLocation = new bukkit.Location(Bukkit.getWorld(location.world.getOrElse(Bukkit.getWorlds.get(0).getName)),
            location.blockX + x, location.blockY + y, location.blockZ + z)
          val block = blockLocation.getWorld.getBlockAt(blockLocation)
          block match {
            case container: Container => container.getInventory.clear()
            case _ =>
          }
          block.setType(Material.AIR)
        })
      })
    })
    rawIsland.adapterAPI.entitiesGetter().enumerateEntity(rawIsland.adapterAPI, cuboid.normalize(rawIsland.adapterAPI).applyLocation(location))
      .filter(_.name != NamespacedKey.minecraft(EntityType.PLAYER.name().toLowerCase).toString).foreach(SpigotEntity.unwrap(_).remove())
  }

  private def placeBlock(index: Int, blockLocation: bukkit.Location): Unit = {
    val block = _blocks(index)
    val material = Material.valueOf(block.name.split(":")(1).toUpperCase)

    blockLocation.getBlock.setType(material)
    blockLocation.getBlock.setData(block.data.toByte)

    if (!block.isBlockEntity) return
    NMSVersioningAPI.getVersionAPI(rawIsland.adapterAPI.plugin).getNBTTools.applyNBT(BlockWrap.of(rawIsland.plugin, blockLocation.getBlock), block.nbt.get)
  }

  private def placeEntity(entity: Entity, islandLocation: Location): Unit = {
    val world = Bukkit.getWorlds.get(0)
    val location = if (cuboid.isNormalized) islandLocation else islandLocation.appendXYZ(-cuboid.minX, -cuboid.minY, -cuboid.minZ)
    val entityLocation = SpigotLocation.unwrap(entity.location.appendXYZ(location.blockX + 0.5, location.blockY + 0.5, location.blockZ + 0.5)
      .withWorld(location.world.getOrElse(world.getName)))
    val entityType = EntityType.valueOf(entity.name.split(':')(1).toUpperCase)
    val bukkitEntity = entityType match {
      case EntityType.DROPPED_ITEM => world.dropItem(entityLocation, new ItemStack(Material.STONE))
      case _ => world.spawnEntity(entityLocation, entityType)
    }
    
    NMSVersioningAPI.getVersionAPI(rawIsland.adapterAPI.plugin).getNBTTools.applyNBT(EntityWrap.of(bukkitEntity), entity.nbt)
    bukkitEntity.teleport(entityLocation)
  }

  private def updateVersion(): Unit = if (version < currentVersion) version = currentVersion

  private def asFile(name: String): File = new File(root, s"$name.island")
}

private case object SpigotIsland {

  val defaultEntitySpawnLocation: bukkit.Location = new bukkit.Location(Bukkit.getWorlds.get(0), 0, 0, 0)

  def locationToInt(location: Location, cuboid: Cuboid): Int = xyzToInt(location.blockX, location.blockY, location.blockZ, cuboid)

  def xyzToInt(x: Int, y: Int, z: Int, cuboid: Cuboid): Int = z + y * cuboid.xSize + x * cuboid.ySize * cuboid.zSize

  def toRawIsland(adapter: AdapterAPI, island: Island): RawIsland =
    new RawIsland(adapter, island.uuid, island.name, island.serverVersion, island.cuboid, island.blocks, island.entities)
}
