package fr.warzou.skyblock.utils.cuboid

import fr.warzou.skyblock.adapter.api.AdapterAPI
import fr.warzou.skyblock.adapter.api.world.{Block, Location}

case class Cuboid(corner0: Location, corner1: Location, blocks: Option[List[Block]] = None) {

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

  def world: Option[String] = {
    (corner0.world, corner1.world) match {
      case (None, None) => None
      case (Some(world0), Some(world1)) => Option.when(world0 == world1)(world0)
      case _ => None
    }
  }

  def enumerateBlocks(adapter: AdapterAPI, world: String): List[Block] = {
    blocks.getOrElse(
      (minX to maxX).foldRight(List[List[Block]]())((face, acc0) => {
        (minY to maxY).foldRight(List[List[Block]]())((line, acc1) => {
          (minZ to maxZ).map(adapter.createLocation(face, line, _).block(world)).toList :: acc1
        }).flatten :: acc0
      }).flatten
    )
  }

  def applyWorld(world: String): Cuboid = Cuboid(corner0.withWorld(world), corner1.withWorld(world))

  def normalize(adapter: AdapterAPI): Cuboid =
    Cuboid(adapter.createLocation(0, 0, 0), adapter.createLocation(xSize, ySize, zSize))

  def atLocation(location: Location): Cuboid = Cuboid(location, location.appendXYZ(xSize, ySize, zSize))
}