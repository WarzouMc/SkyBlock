package fr.warzou.skyblock.utils.cuboid

import fr.warzou.skyblock.adapter.api.AdapterAPI
import fr.warzou.skyblock.adapter.api.core.world.{Block, Location}

/**
 * Representation of cuboid mathematical object.
 *
 * @param corner0 first corner who represent this cuboid
 * @param corner1 second corner who represent this cuboid
 * @param blocks Optionally a list of block for this cuboid
 *
 * @version 0.0.1
 * @author Warzou
 */
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

  {
    if (blocks.isDefined && blocks.get.length != blockCount)
      throw new IllegalArgumentException(s"The size of the block list does not match the number of blocks in this cuboid " +
        s"(found ${blocks.get.length}, expect $blockCount) !")
  }

  /**
   * Create the smallest location in this cuboid
   * @param adapter an AdapterAPI impl
   * @return a location ([[minX]], [[minY]], [[minZ]])
   */
  def minCorner(adapter: AdapterAPI): Location = adapter.createLocation(minX, minY, minZ)

  /**
   * Create the tallest location in this cuboid
   * @param adapter an AdapterAPI impl
   * @return a location ([[maxX]], [[maxY]], [[maxZ]])
   */
  def maxCorner(adapter: AdapterAPI): Location = adapter.createLocation(maxX, maxY, maxZ)

  /**
   * Returns this cuboid world is is correctly defined.
   *
   * Correctly defined mean the both corner has a defined world or these worlds are equals.
   * @return cuboid world if is correctly defined, None else
   */
  def world: Option[String] = {
    (corner0.world, corner1.world) match {
      case (None, None) => None
      case (Some(world0), Some(world1)) => Option.when(world0 == world1)(world0)
      case _ => None
    }
  }

  /**
   * Enumerate all blocks in this cuboid in a targeted world.
   *
   * @param adapter an AdapterAPi impl
   * @param world a world name
   * @return an enumeration of all blocks in this cuboid in a targeted world.
   */
  def enumerateBlocks(adapter: AdapterAPI, world: String): List[Block] = {
    blocks.getOrElse(
      (minX to maxX).foldRight(List[List[Block]]())((face, acc0) => {
        (minY to maxY).foldRight(List[List[Block]]())((line, acc1) => {
          (minZ to maxZ).map(adapter.createLocation(face, line, _).block(world)).toList :: acc1
        }).flatten :: acc0
      }).flatten
    )
  }

  /**
   * Create a new cuboid with same properties but in another world.
   * @param world world name
   * @return a new cuboid with a other world
   */
  def applyWorld(world: String): Cuboid = Cuboid(corner0.withWorld(world), corner1.withWorld(world))

  /**
   * Create a new Cuboid with [[Location.appendLocation]] apply to corners.
   * @param location location at append
   * @return a Cuboid with [[Location.appendLocation]] apply to corners
   */
  def appendLocation(location: Location): Cuboid = Cuboid(corner0.appendLocation(location), corner1.appendLocation(location), blocks)

  /**
   * Returns a new Cuboid with [[minX]] = [[minY]] = [[minZ]] = 0 and [[maxX]] = [[xSize]], [[maxY]] = [[ySize]], [[maxZ]] = [[zSize]]
   * @param adapter a AdapterAPI impl
   * @return a Cuboid with [[minX]] = [[minY]] = [[minZ]] = 0 and [[maxX]] = [[xSize]], [[maxY]] = [[ySize]], [[maxZ]] = [[zSize]]
   */
  def normalize(adapter: AdapterAPI): Cuboid =
    Cuboid(adapter.createLocation(0, 0, 0), adapter.createLocation(xSize, ySize, zSize))

  /**
   * @return [[minX]] = [[minY]] = [[minZ]] = 0
   */
  def isNormalized: Boolean = minX == 0 && minY == 0 && minZ == 0

  /**
   * Returns a new Cuboid from this with [[corner0]] = location
   * @param location a location
   * @return a new Cuboid from this with [[corner0]] = location
   */
  def atLocation(location: Location): Cuboid = Cuboid(location, location.appendXYZ(xSize, ySize, zSize))
}