package fr.warzou.skyblock.nms.versioning.api.core.world.chunk

import fr.warzou.skyblock.adapter.api.core.entity.Entity

trait NMSChunk {

  def isLoaded: Boolean

  def sections: Array[NMSChunkSection]

  def entities: (Int, Array[Byte])

  def tileEntities: (Int, Array[Byte])

  def tileTicks: (Int, Array[Byte])

  def toCustomChunkFormat: Array[Byte]

}
