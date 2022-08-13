package fr.warzou.skyblock.adapter.api.core.world

import fr.warzou.skyblock.adapter.api.common.wrap.{Unwrapper, Wrappable, Wrapper}

/**
 * A wrap of minecraft location.
 *
 * @version 0.0.1
 * @author Warzou
 */
trait Location extends Wrappable[Location] {

  /**
   * Returns an optionally world if is defined.
   * @return Optionally world if is defined
   */
  def world: Option[String]

  /**
   * @return a rounding of [[x]]
   */
  def blockX: Int

  /**
   * @return a rounding of [[y]]
   */
  def blockY: Int

  /**
   * @return a rounding of [[z]]
   */
  def blockZ: Int

  def x: Double
  def y: Double
  def z: Double

  /**
   * Returns a new location with same coordinate and a different world.
   * @param world new world name
   * @return a new location with same coordinate and a different world
   */
  def withWorld(world: String): Location

  /**
   * Return the block at this location.
   * @return optionally block if world is defined
   */
  def block: Option[Block] = world.map(block)

  /**
   * Returns the block at this location coordinate in a target world.
   * @param world a world name
   * @return the block at this location coordinate in a target world
   */
  def block(world: String): Block

  /**
   * Returns a new location with coordinate of this was add with another location
   * @param location a location
   * @return new location with coordinate of this was add with another location
   */
  def appendLocation(location: Location): Location = appendXYZ(location.x, location.y, location.z)

  /**
   * Create a new location by adding the coordinates with x, y, z.
   * @param x append to x
   * @param y append to y
   * @param z append to z
   * @return a new location with coordinate equals to this + (x, y, z)
   */
  def appendXYZ(x: Double, y: Double, z: Double): Location

  override def toString: String = s"Location{world=$world, x=$x, y=$y, z=$z}"
}