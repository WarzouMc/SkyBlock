package fr.warzou.skyblock.nms.versioning.api.core.world.chunk

import fr.warzou.skyblock.nms.versioning.api.utils.io.{Nibble, NibbleArray}

trait NMSChunkSection {

  def y: Byte

  def blocks: Array[Short]

  def datas: NibbleArray

  def blockLights: NibbleArray

  def skyLights: NibbleArray

  def toByteArray: Array[Byte]
}
