package fr.warzou.skyblock.format.island.reader

import fr.warzou.skyblock.adapter.api.AdapterAPI
import fr.warzou.skyblock.adapter.api.common.wrap.{Unwrapper, Wrapper}
import fr.warzou.skyblock.adapter.api.core.entity.Entity
import fr.warzou.skyblock.adapter.api.core.world.block.Block
import fr.warzou.skyblock.adapter.api.core.world.location.Location
import fr.warzou.skyblock.format.island.core.RawIsland
import fr.warzou.skyblock.format.island.core.io.Reader
import fr.warzou.skyblock.utils.cuboid.Cuboid
import fr.warzou.skyblock.utils.{IOUtils, ServerVersion}

import java.io.{EOFException, File, FileInputStream, FileReader}
import java.nio.ByteBuffer
import java.nio.charset.StandardCharsets
import java.util.UUID

case class IslandFileReader(adapterAPI: AdapterAPI, fileName: String) extends Reader {

  private val plugin = adapterAPI.plugin
  private val root = new File(plugin.dataFolder, "islands")
  private val reader = new FileInputStream(new File(root, s"$fileName"))

  override def read: RawIsland = {
    plugin.logger.log(s"Read island file '$fileName'.")
    val version = readVersion()
    val uuid = readUUID()
    val islandName = readString()
    val usedBlock = readBlocks()
    val cuboid = readCuboid()

    val blocks = new Array[Block](cuboid.blockCount)
    (0 until cuboid.blockCount).foreach(blocks(_) = usedBlock(IOUtils.readVarInt(reader)))

    val entities = readEntities()
    reader.close()

    plugin.logger.log(s"Island '$uuid' with name '$islandName' was successfully read from file '$fileName'.")
    RawIsland(adapterAPI, uuid, islandName, version, cuboid, blocks.toList, entities)
  }

  private def readVersion(): ServerVersion = ServerVersion(readByte(), readByte(), readByte())

  private def readUUID(): UUID = {
    val array: Array[Byte] = readNByte(16).map(_.toByte)
    val byteBuffer = ByteBuffer.wrap(array)
    new UUID(byteBuffer.getLong, byteBuffer.getLong)
  }

  private def readCuboid(): Cuboid = {
    val location0 = adapterAPI.createLocation(0, 0, 0)
    val x = readByte()
    val z = readByte()
    val y = readByte()

    val location1 = adapterAPI.createLocation(x - 1, y - 1, z - 1)
    Cuboid(location0, location1)
  }

  private def readBlocks(): Array[Block] = {
    val blockEntityCount = readByte()
    val blockEntities = new Array[Array[Byte]](blockEntityCount - 1)
    (0 until blockEntityCount - 1).foreach(blockEntities(_) = readNByte(readU2Short()).map(_.toByte))
    val usedBlockCount = readByte()
    val usedBlocks = new Array[Block](usedBlockCount)
    (0 until usedBlockCount).foreach(usedBlocks(_) =
      new Block {
        private val _name = readString()
        private val _data = readByte()
        private val nbtIndex = readByte()

        override def name: String = _name

        override def data: Int = _data

        override def isBlockEntity: Boolean = nbtIndex != blockEntityCount

        override def nbt: Option[Array[Byte]] = if (isBlockEntity) Some(blockEntities(nbtIndex)) else None

        override def wrapper(): Wrapper[_, Block] = adapterAPI.wrapperOf(this)

        override def unwrapper(): Unwrapper[Block, _] = adapterAPI.unwrapperOf(this)
      }
    )
    usedBlocks
  }

  private def readEntities(): List[Entity] = {
    val count = readByte()
    val entities = new Array[Entity](count)
    (0 until count).foreach(i => {
      val x = readByte()
      val y = readByte()
      val z = readByte()

      entities(i) = new Entity {
        private val _location = adapterAPI.createLocation(x.toDouble, y.toDouble, z.toDouble)
        private val entityName = readString()
        private val _nbt = readNByte(readU2Short()).map(_.toByte)

        override def location: Location = _location

        override def name: String = entityName

        override def nbt: Array[Byte] = _nbt

        override def wrapper(): Wrapper[_, Entity] = adapterAPI.wrapperOf(this)

        override def unwrapper(): Unwrapper[Entity, _] = adapterAPI.unwrapperOf(this)
      }
    })
    entities.toList
  }

  private def readByte(): Int = {
    val int = reader.read()
    if (int == -1) throw new EOFException()
    int
  }

  private def readNByte(length: Int): Array[Int] = {
    val array = new Array[Int](length)
    (0 until length).foreach(array(_) = readByte())
    array
  }

  private def readString(): String = {
    val length = readByte()
    new String(readNByte(length).map(_.toByte), StandardCharsets.UTF_8)
  }

  private def readU2Short(): Short = {
    val array = readNByte(2)
    ByteBuffer.wrap(array.map(_.toByte)).getShort
  }

  private def readU8Long(): Long = ByteBuffer.wrap(readNByte(8).map(_.toByte)).getLong
}