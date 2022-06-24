package fr.warzou.skyblock.utils.cuboid

import fr.warzou.skyblock.adapter.api.AdapterAPI
import fr.warzou.skyblock.adapter.api.world.Location

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

  def minCorner(adapter: AdapterAPI): Location = adapter.createLocation(minX, minY, minZ)
  def maxCorner(adapter: AdapterAPI): Location = adapter.createLocation(maxX, maxY, maxZ)

  val blockCount: Int = xSize * ySize * zSize
}
