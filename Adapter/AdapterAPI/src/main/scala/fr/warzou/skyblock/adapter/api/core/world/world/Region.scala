package fr.warzou.skyblock.adapter.api.core.world.world

abstract case class Region(x: Int, z: Int, mca: Array[Byte]) {
  def compressMCA(): Array[Byte]
}
