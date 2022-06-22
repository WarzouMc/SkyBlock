package fr.warzou.island.format.writer.file

import fr.warzou.island.format.core.RawIsland
import fr.warzou.island.format.core.common.Version
import fr.warzou.island.format.core.common.cuboid.Cuboid
import fr.warzou.island.format.reader.IslandFileReader
import net.minecraft.server.v1_12_R1.AxisAlignedBB
import org.bukkit.block.Block
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld
import org.bukkit.entity.Entity
import org.bukkit.plugin.Plugin
import org.bukkit.{Bukkit, Location}

import java.io.{File, FileOutputStream}
import scala.jdk.CollectionConverters._

class IslandFileWriter(plugin: Plugin, name: String, cuboid: Cuboid) {

  private val root = new File(plugin.getDataFolder, "islands")

  def writeIsland(): RawIsland = {
    val folder = createFolder()
    val islandFile = createFile(folder)
    val outputStream = new FileOutputStream(islandFile)
    val writer = new Writer(outputStream, Version.from(plugin), cuboid, enumerateBlocks(), enumerateEntities())
    writer.write()
    new IslandFileReader(plugin, name).read()
  }

  private def enumerateBlocks(): List[Block] = {
    (cuboid.minX to cuboid.maxX).foldRight(List[List[Block]]())((face, acc0) => {
      (cuboid.minY to cuboid.minY).foldRight(List[List[Block]]())((line, acc1) => {
        (cuboid.minZ to cuboid.maxZ).map(Bukkit.getWorlds.get(0).getBlockAt(face, line, _)).toList :: acc1
      }).flatten :: acc0
    }).flatten
  }

  private def enumerateEntities(): List[Entity] = {
    val bb: AxisAlignedBB = new AxisAlignedBB(cuboid.minX, cuboid.minY, cuboid.minZ, cuboid.maxX, cuboid.maxY, cuboid.maxZ)
    val entityList: java.util.List[net.minecraft.server.v1_12_R1.Entity] = cuboid.minCorner.getWorld.asInstanceOf[CraftWorld]
      .getHandle.getEntities(null, bb, (e: net.minecraft.server.v1_12_R1.Entity) => true)
    entityList.asScala.map(_.getBukkitEntity).toList
  }

  private def createFolder(): File = {
    createIslandsRoot()
    val islandRoot = new File(root, name)
    islandRoot.mkdirs()
    islandRoot
  }

  private def createFile(islandRoot: File): File = {
    createIslandsRoot()
    val islandFile = new File(root, s"$name.island")
    if  (!islandFile.exists()) islandFile.createNewFile()
    islandFile
  }

  private def createIslandsRoot(): Unit = {
    plugin.getDataFolder.mkdirs()
    root.mkdirs()
  }

}
