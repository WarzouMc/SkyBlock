package fr.warzou.island.format.reader
import fr.il_totore.spigotmetadata.api.SpigotMetadataAPI
import fr.il_totore.spigotmetadata.api.nbt.{NBTInputStream, NBTTagCompound, NBTTagType}
import fr.warzou.island.format.core.RawIsland
import fr.warzou.island.format.core.common.Version
import fr.warzou.island.format.core.common.block.FileBlock
import fr.warzou.island.format.core.common.entity.FileEntity
import fr.warzou.skyblock.adapter.api.AdapterAPI
import fr.warzou.skyblock.adapter.api.world.Location
import fr.warzou.skyblock.utils.IOUtils
import fr.warzou.skyblock.utils.cuboid.Cuboid

import java.io.{ByteArrayInputStream, File, FileReader}
import java.nio.ByteBuffer
import java.nio.charset.StandardCharsets

class IslandFileReader(val adapterAPI: AdapterAPI, val name: String) {

  private val plugin = adapterAPI.getPlugin
  private val root = new File(plugin.getDataFolder, "islands")
  private val reader = new FileReader(new File(root, name))
  private val nbtManager = SpigotMetadataAPI.getAPI.getNBTManager

  def read(): RawIsland = {
    val version = readVersion()
    val cuboid = readCuboid()
    val blocks = readBlocks(cuboid)
    val entities = readEntities()
    new RawIsland(plugin, name, version, cuboid, blocks, entities)
  }

  private def readVersion(): Version = Version(readByte(), readByte(), readByte())

  private def readCuboid(): Cuboid = {
    val location0 = adapterAPI.createLocation(0, 0, 0)
    val x = readByte()
    val z = readByte()
    val y = readByte()
    val location1 = adapterAPI.createLocation(x, y , z)
    new Cuboid(location0, location1)
  }

  private def readBlocks(cuboid: Cuboid): List[FileBlock] = {
    val blockEntityCount = readU2Short()
    val blockEntities = new Array[NBTTagCompound](blockEntityCount - 1)
    (0 until blockEntityCount - 1).foreach(blockEntities(_) = readNBT().readTag(NBTTagType.COMPOUND))
    val usedBlockCount = readU2Short()
    val usedBlocks = new Array[FileBlock](usedBlockCount - 1)
    (0 until blockEntityCount - 1).foreach(usedBlocks(_) = new FileBlock(readString(), readByte(),
      {
        val index = readU2Short()
        if (index == blockEntityCount) None
        else Some(blockEntities(index))
      }))

    val blocks = new Array[FileBlock](cuboid.blockCount)
    (0 until cuboid.blockCount).foreach(blocks(_) = usedBlocks(IOUtils.readVarInt(reader)))
    blocks.toList
  }

  private def readEntities(): List[FileEntity] = {
    val count = readByte()
    val entities = new Array[FileEntity](count)
    (0 until count).foreach(i => {
      val long = readU8Long()
      val x = long >> 38
      val y = long & 0xFFF
      val z = (long >> 12) & 0x3FFFFFF
      val entityType = EntityType.values()(readByte())
      val nbt = readNBT().readTag(NBTTagType.COMPOUND)
      entities(i) = new FileEntity(new Location(Bukkit.getWorlds.get(0), x, y, z), entityType, nbt)
    })
    entities.toList
  }

  private def readNBT(): NBTInputStream = {
    val length = readU2Short()
    val array = readNByte(length)
    new NBTInputStream(nbtManager, new ByteArrayInputStream(array))
  }

  private def readByte(): Byte = {
    val int = readByte()
    if (int == -1) throw new IndexOutOfBoundsException("Try to read a byte after the file end !")
    int
  }

  private def readNByte(length: Int): Array[Byte] = {
    val array = new Array[Byte](length)
    (0 until length).foreach(array(_) = readByte())
    array
  }

  private def readString(): String = {
    val length = readByte()
    new String(readNByte(length), StandardCharsets.UTF_8)
  }

  private def readU2Short(): Short = ByteBuffer.wrap(readNByte( 2)).getShort

  private def readU8Long(): Long = ByteBuffer.wrap(readNByte( 8)).getLong
}