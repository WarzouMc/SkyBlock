package fr.warzou.spigot.skyblock.main

import fr.warzou.island.format.core.RawIsland
import fr.warzou.island.format.core.common.Version
import fr.warzou.island.format.core.common.cuboid.Cuboid
import fr.warzou.island.format.writer.file.Writer
import org.bukkit.block.Block
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld
import org.bukkit.entity.Entity
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.{Bukkit, Location}

class SkyBlock extends JavaPlugin {

  override def onEnable(): Unit = {
    val loc0 = new Location(Bukkit.getWorlds.get(0), 0, 100, 0)
    val loc1 = new Location(Bukkit.getWorlds.get(0), 10, 107, 10)
    new Writer(this).write(new RawIsland("OWO", Version.from(this), new Cuboid(loc0, loc1), coordToList(loc0, loc1), entities(loc0, loc1)))
  }

  private def coordToList(location0: Location, location1: Location): List[Block] = {
    (location0.getBlockX to location1.getBlockX).foldRight(List[List[Block]]())((value, acc) => faceToList(location0, location1, value) :: acc).flatten
  }

  private def faceToList(location0: Location, location1: Location, face: Int): List[Block] = {
    (location0.getBlockY to location1.getBlockY).foldRight(List[List[Block]]())((value, acc) => lineToList(location0, location1, face, value) :: acc).flatten
  }

  private def lineToList(location0: Location, location1: Location, face: Int, line: Int): List[Block] = {
    (location0.getBlockZ to location1.getBlockZ).map(Bukkit.getWorlds.get(0).getBlockAt(face, line, _)).toList
  }

  private def entities(location0: Location, location1: Location): List[Entity] = {
    import net.minecraft.server.v1_12_R1.AxisAlignedBB

    import java.util
    val bb: AxisAlignedBB = new AxisAlignedBB(location0.getX, location0.getY, location0.getZ, location1.getX, location1.getY, location1.getZ)
    val entityList: util.List[net.minecraft.server.v1_12_R1.Entity] = location0.getWorld.asInstanceOf[CraftWorld].getHandle.getEntities(null, bb, (e: net.minecraft.server.v1_12_R1.Entity) => true)

    import scala.jdk.CollectionConverters._
    entityList.asScala.map(_.getBukkitEntity).toList
  }
}
