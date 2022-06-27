package fr.warzou.skyblock.utils.cuboid

import fr.warzou.skyblock.adapter.api.AdapterAPI
import fr.warzou.skyblock.adapter.api.world.{Block, Location}

class Cuboid(corner0: Location, corner1: Location) {

  val xSize: Int = Math.abs(corner1.blockX - corner0.blockX) + 1
  val ySize: Int = Math.abs(corner1.blockY - corner0.blockY) + 1
  val zSize: Int = Math.abs(corner1.blockZ - corner0.blockZ) + 1

  val minX: Int = Math.min(corner0.blockX, corner1.blockX)
  val maxX: Int = Math.max(corner0.blockX, corner1.blockX)

  val minY: Int = Math.min(corner0.blockY, corner1.blockY)
  val maxY: Int = Math.max(corner0.blockY, corner1.blockY)

  val minZ: Int = Math.min(corner0.blockZ, corner1.blockZ)
  val maxZ: Int = Math.max(corner0.blockZ, corner1.blockZ)

  val blockCount: Int = xSize * ySize * zSize

  def minCorner(adapter: AdapterAPI): Location = adapter.createLocation(minX, minY, minZ)
  def maxCorner(adapter: AdapterAPI): Location = adapter.createLocation(maxX, maxY, maxZ)

  def enumerateBlocks(adapter: AdapterAPI, world: String): List[Block] = {
    (minX to maxX).foldRight(List[List[Block]]())((face, acc0) => {
      (minY to maxY).foldRight(List[List[Block]]())((line, acc1) => {
        (minZ to maxZ).map(adapter.createLocation(face, line, _).block(world)).toList :: acc1
      }).flatten :: acc0
    }).flatten
  }
}
