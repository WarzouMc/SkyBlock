package fr.warzou.island.format.reader
import fr.il_totore.spigotmetadata.api.SpigotMetadataAPI
import fr.il_totore.spigotmetadata.api.nbt.NBTInputStream
import fr.warzou.island.format.core.RawIsland
import fr.warzou.island.format.core.common.Version
import org.bukkit.block.Block
import org.bukkit.plugin.Plugin
import org.jetbrains.annotations.NotNull

import java.io.{File, FileReader}
import java.nio.ByteBuffer

class IslandFileReader(val plugin: Plugin) {

  private val root = new File(plugin.getDataFolder, "islands")
  private val nbtManager = SpigotMetadataAPI.getAPI.getNBTManager

  def read(@NotNull name: String): RawIsland = read(new File(root, name))

  def read(@NotNull file: File): RawIsland = {
    val reader: FileReader = new FileReader(file)
    val version = readVersion(reader)
    val blocks = readBlocks(reader)

    ???
  }

  private def readVersion(reader: FileReader): Version = new Version(reader.read(), reader.read(), reader.read())

  private def readBlocks(reader: FileReader): List[Block] = {
    val blockEntityCount = readShort(reader)
    val blockEntities = new Array[NBTInputStream](blockEntityCount - 1)
    (0 until blockEntityCount - 1).foreach(blockEntities(_) = readNBT(reader))

    ???
  }

  def readNBT(reader: FileReader): NBTInputStream = {
    val length = readShort(reader)
    val array = readNByte(reader, length)
    //new NBTInputStream(nbtManager, new ByteArrayInputStream(array))
    ???
  }

  private def readByte(reader: FileReader): Byte = {
    val int = reader.read
    if (int == -1) throw new IndexOutOfBoundsException("Try to read a byte after the file end !")
    int.toByte
  }

  private def readNByte(reader: FileReader, length: Int): Array[Byte] = {
    val array = new Array[Byte](length)
    (0 until length).foreach(array(_) = readByte(reader))
    array
  }

  private def readShort(reader: FileReader): Short = ByteBuffer.wrap(readNByte(reader, 2)).getShort
}
