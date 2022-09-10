package fr.warzou.skyblock.adapter.api.core.world.world

trait Chunk {

  def x: Int

  def z: Int

  def worldX: Int = x * 16

  def worldZ: Int = z * 16

  def toByteArray: Array[Byte]

}
