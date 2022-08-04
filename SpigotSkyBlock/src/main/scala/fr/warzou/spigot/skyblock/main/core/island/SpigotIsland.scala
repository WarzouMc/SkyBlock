package fr.warzou.spigot.skyblock.main.core.island

import fr.warzou.island.format.core.RawIsland
import fr.warzou.skyblock.adapter.api.AdapterAPI
import fr.warzou.skyblock.adapter.api.core.entity.Entity
import fr.warzou.skyblock.adapter.api.core.world.{Block, Location}
import fr.warzou.skyblock.api.common.module.ModuleHandler
import fr.warzou.skyblock.api.core.island.Island
import fr.warzou.skyblock.api.core.modules.island.IslandModule
import fr.warzou.skyblock.utils.ServerVersion
import fr.warzou.skyblock.utils.cuboid.Cuboid
import fr.warzou.spigot.skyblock.main.core.island.SpigotIsland.{locationToInt, xyzToInt}
import net.minecraft.server.v1_12_R1.{BlockPosition, NBTCompressedStreamTools}
import org.bukkit
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld
import org.bukkit.{Bukkit, Material}

import java.io.{ByteArrayInputStream, File}
import java.util.UUID
import scala.collection.mutable.ListBuffer

case class SpigotIsland(private val moduleHandler: ModuleHandler, private val rawIsland: RawIsland, private val optionFile: Option[File]) extends Island {

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

  //todo place entity
  override def place(location: Location): Unit = {
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
  }

  override def withFileName(name: String): Unit = {
    val islandModule = moduleHandler.getModule[IslandModule](classOf[IslandModule]).get
    if (islandModule.islandByFileName(name).isDefined)
      throw new IllegalArgumentException(s"Already took file name for $name!")
    _file = asFile(name)
    islandModule.setFile(this, name)
  }

  //todo delete old
  override def fileName: String = _file.getName

  override def file: File = _file

  override def save(): Boolean = {
    if (_blocks.equals(rawIsland.blocks) && _entities.equals(rawIsland.entities)) return false

    val newRawIsland = RawIsland(rawIsland.adapterAPI, uuid, name, serverVersion, cuboid, blocks, entities)
    newRawIsland.saveAs(_file.getName)
    true
  }

  private def placeBlock(index: Int, blockLocation: bukkit.Location): Unit = {
    val world = blockLocation.getWorld
    val block = _blocks(index)
    val material = Material.valueOf(block.name.split(":")(1))

    world.getBlockAt(blockLocation).setType(material)
    world.getBlockAt(blockLocation).setData(block.data.toByte)

    if (!block.isBlockEntity) return
    applyTagToBlock(block, blockLocation)
  }

  private def applyTagToBlock(block: Block, blockLocation: bukkit.Location): Unit = {
    val nbt = block.nbt.get
    val inputStream = new ByteArrayInputStream(nbt)
    val nbtTagCompound = NBTCompressedStreamTools.a(inputStream)
    val worldServer = blockLocation.getWorld.asInstanceOf[CraftWorld].getHandle
    val tileEntity = worldServer.getTileEntity(new BlockPosition(blockLocation.getBlockX, blockLocation.getBlockY, blockLocation.getBlockZ))

    tileEntity.load(nbtTagCompound)
  }

  private def updateVersion(): Unit = if (version < currentVersion) version = currentVersion

  private def asFile(name: String): File = new File(root, s"$name.island")
}

private case object SpigotIsland {

  def locationToInt(location: Location, cuboid: Cuboid): Int = xyzToInt(location.blockX, location.blockY, location.blockZ, cuboid)

  def xyzToInt(x: Int, y: Int, z: Int, cuboid: Cuboid): Int = z + y * cuboid.xSize + x * cuboid.ySize * cuboid.zSize

  def toRawIsland(adapter: AdapterAPI, island: Island): RawIsland =
    new RawIsland(adapter, island.uuid, island.name, island.serverVersion, island.cuboid, island.blocks, island.entities)
}
