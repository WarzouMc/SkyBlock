package fr.warzou.skyblock.nms.versioning.api.core.world.chunk

import fr.warzou.skyblock.nms.versioning.api.utils.io.Nibble

trait NMSChunkSection {

  def y: Int

  def blockLights: Array[Nibble]

  def blocks: Array[Byte]

  def datas: Array[Nibble]

  def skyLights: Array[Nibble]
}
