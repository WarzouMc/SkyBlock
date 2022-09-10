package fr.warzou.skyblock.nms.versioning.api.core.world.chunk

trait NMSChunk {

  def isLoaded: Boolean

  def sections: Array[NMSChunkSection]

  def toCustomChunkFormat: Array[Byte]

}
