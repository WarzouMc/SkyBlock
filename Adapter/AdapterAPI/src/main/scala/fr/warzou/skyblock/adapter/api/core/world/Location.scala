package fr.warzou.skyblock.adapter.api.core.world

import fr.warzou.skyblock.adapter.api.common.wrap.{Unwrapper, Wrappable, Wrapper}

trait Location extends Wrappable[Location] {

  def world: Option[String]

  def blockX: Int
  def blockY: Int
  def blockZ: Int

  def x: Double
  def y: Double
  def z: Double

  def locationInWorld(world: String): Location

  def block: Block = {
    if (world.isEmpty) throw new IllegalStateException("Cannot found world !")
    block(world.get)
  }

  def block(world: String): Block

  def withWorld(_world: String): Location = {
    val current = this
    new Location {
      override def world: Option[String] = Some(_world)

      override def blockX: Int = current.blockX
      override def blockY: Int = current.blockY
      override def blockZ: Int = current.blockZ

      override def x: Double = current.x
      override def y: Double = current.y
      override def z: Double = current.z

      override def locationInWorld(world: String): Location = current.locationInWorld(world)

      override def block(world: String): Block = current.block(world)

      override def appendXYZ(x: Double, y: Double, z: Double): Location = current.appendXYZ(x, y, z)

      override def toString: String = s"Location{world=$world, x=$x, y=$y, z=$z}"

      override def wrapper(): Wrapper[_, Location] = current.wrapper()

      override def unwrapper(): Unwrapper[Location, _] = current.unwrapper()
    }
  }

  def appendLocation(location: Location): Location = appendXYZ(location.x, location.y, location.z)

  def appendXYZ(x: Double, y: Double, z: Double): Location

  override def toString: String = s"Location{world=$world, x=$x, y=$y, z=$z}"
}