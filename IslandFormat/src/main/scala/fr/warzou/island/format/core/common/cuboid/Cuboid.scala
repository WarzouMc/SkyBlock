package fr.warzou.island.format.core.common.cuboid

import org.bukkit.{Bukkit, Location}

class Cuboid(corner0: Location, corner1: Location) {

  val xSize: Int = Math.abs(corner1.getBlockX - corner0.getBlockX)
  val ySize: Int = Math.abs(corner1.getBlockY - corner0.getBlockY)
  val zSize: Int = Math.abs(corner1.getBlockZ - corner0.getBlockZ)

  val minX: Int = Math.min(corner0.getBlockX, corner1.getBlockX)
  val maxX: Int = Math.max(corner0.getBlockX, corner1.getBlockX)

  val minY: Int = Math.min(corner0.getBlockY, corner1.getBlockY)
  val maxY: Int = Math.max(corner0.getBlockY, corner1.getBlockY)

  val minZ: Int = Math.min(corner0.getBlockZ, corner1.getBlockZ)
  val maxZ: Int = Math.max(corner0.getBlockZ, corner1.getBlockZ)

  val minCorner = new Location(Bukkit.getWorlds.get(0), minX, minY, minZ)
  val maxCorner = new Location(Bukkit.getWorlds.get(0), maxX, maxY, maxZ)

  val blockCount: Int = xSize * ySize * zSize
}
