package fr.warzou.skyblock.adapter.api.world

trait Location {

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
}