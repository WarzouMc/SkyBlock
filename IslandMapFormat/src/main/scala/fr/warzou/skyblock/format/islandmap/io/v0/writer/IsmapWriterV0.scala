package fr.warzou.skyblock.format.islandmap.io.v0.writer

import fr.warzou.skyblock.adapter.api.AdapterAPI
import fr.warzou.skyblock.adapter.api.core.entity.Player
import fr.warzou.skyblock.adapter.api.core.world.location.Location
import fr.warzou.skyblock.adapter.api.core.world.sector.Sector
import fr.warzou.skyblock.adapter.api.core.world.world.{Dimension, Region, World}
import fr.warzou.skyblock.format.islandmap.core.{IndividualWorld, IslandMap, SectorMap}
import fr.warzou.skyblock.format.islandmap.core.io.Writer
import fr.warzou.skyblock.utils.{ServerVersion, TickUtils}
import org.jetbrains.annotations.NotNull

import java.io.{File, FileInputStream, OutputStream}
import java.nio.ByteBuffer
import java.util.UUID

class IsmapWriterV0(adapter: AdapterAPI) extends Writer {
  private var outputStream: OutputStream = _
  private var islandMap: IslandMap = _

  override def version: Int = 0

  override def write(@NotNull _outputStream: OutputStream, @NotNull map: IslandMap): Unit = {
    outputStream = _outputStream
    islandMap = map

    writeCommonInfo()
    writeStatistics()
    if (mapType() == 0) writeWorld() else writeSectors()
  }

  private def writeCommonInfo(): Unit = {
    write(version)
    write(mapType())
    val serverVersion = ServerVersion.from(adapter.plugin.version)
    write(serverVersion.major)
    write(serverVersion.minor)
    write(serverVersion.revision)

    writeLong(islandMap.creation)
    writeLong(TickUtils.millisToTick(System.currentTimeMillis()))

    writeUUID(islandMap.uuid)
    writeString(islandMap.name)
  }

  private def writeStatistics(): Unit = {
    writeUUID(islandMap.startIsland)
    writeLocation(islandMap.spawnLocation.getOrElse(adapter.createLocation(0, 0, 0)))
    writeLocation(islandMap.memberSpawnLocation.getOrElse(adapter.createLocation(0, 0, 0)))

    write(islandMap.players.length)
    islandMap.players.foreach(writePlayer)
  }

  private def writeLocation(location: Location): Unit = {
    val x = java.lang.Double.doubleToLongBits(location.x)
    val y = java.lang.Float.floatToIntBits(location.y.toFloat)
    val z = java.lang.Double.doubleToLongBits(location.z)

    writeLong(x)
    writeInt(y)
    writeLong(z)
  }

  private def writePlayer(player: Player): Unit = {
    writeUUID(player.uuid)
    writeLong(player.memberSince)
    writeLong(player.playTime)
    write(player.permissionLevel)
    write(if (player.isFlying) 1 else 0)
    write(player.dim)
    write(player.foodLevel)
    write(player.foodExhaustionLevel)
    write(player.foodSaturationLevel)
    write(player.foodTickTimer)
    writeNBT(player.riddenEntity.map(_.nbt).getOrElse(Array.emptyByteArray))
    writeNBT(player.shoulderEntityLeft.map(_.nbt).getOrElse(Array.emptyByteArray))
    writeNBT(player.shoulderEntityRight.map(_.nbt).getOrElse(Array.emptyByteArray))
    writeInt(player.xpLevel)
    writeInt(java.lang.Float.floatToIntBits(player.xpPercent))
    writeInt(player.xpSeed)
    writeInt(player.xpTotal)
  }

  private def writeWorld(): Unit = {
    val world = islandMap.asInstanceOf[IndividualWorld].world
    writeString(world.name)
    writeFile(world.level)
    write(world.dimCount)
    world.dimensions.foreach(writeDimension)
  }

  private def writeDimension(dimension: Dimension): Unit = {
    write(if (dimension.isCustom) 1 else 0)
    writeString(dimension.name)
    write(dimension.id)
    writeShort(dimension.regionCount.toShort)
    dimension.regions().foreach(writeRegion)
  }

  private def writeRegion(region: Region): Unit = {
    println("write region")
    writeInt(region.x)
    writeInt(region.z)
    writeInt(region.mca.length)
    writeArray(region.compressMCA())
  }

  private def writeSectors(): Unit = islandMap.asInstanceOf[SectorMap].sectors.foreach(writeSector)

  private def writeSector(sector: Sector): Unit = {
    writeLocation(sector.location)
    writeShort(sector.width.toShort)
    writeShort(sector.length.toShort)
  }

  private def writeUUID(uuid: UUID): Unit = {
    writeLong(uuid.getMostSignificantBits)
    writeLong(uuid.getLeastSignificantBits)
  }

  private def writeInt(int: Int): Unit = writeArray(ByteBuffer.allocate(4).putInt(int).array())

  private def writeLong(long: Long): Unit = writeArray(ByteBuffer.allocate(8).putLong(long).array())

  private def writeShort(short: Short): Unit = writeArray(ByteBuffer.allocate(2).putShort(short).array())

  private def writeString(string: String): Unit = {
    val array = string.getBytes
    write(array.length)
    writeArray(string.getBytes)
  }

  private def writeNBT(nbt: Array[Byte]): Unit = {
    writeShort(nbt.length.toShort)
    writeArray(nbt)
  }

  private def writeFile(file: File): Unit = {
    val inputStream = new FileInputStream(file)
    writeArray(inputStream.readAllBytes())
  }

  private def writeArray(array: Array[Byte]): Unit = outputStream.write(array)

  private def write(int: Int): Unit = outputStream.write(int)

  private def mapType(): Int = islandMap match {
    case SectorMap(_, _, _, _, _, _, _, _, _) => 1
    case IndividualWorld(_, _, _, _, _, _, _, _, _) => 0
  }
}
