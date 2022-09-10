package fr.warzou.skyblock.adapter.api.core.world.world

trait Region {

  def dimension: Dimension

  def x: Int

  def z: Int

  def chunkX: Int = x * 32

  def chunkZ: Int = z * 32

  def worldX: Int = chunkX * 16

  def worldZ: Int = chunkZ * 16

  def chunks: Array[Chunk]
}
