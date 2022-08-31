package fr.warzou.skyblock.nms.versioning.api.core.world.chunk

trait NMSChunk {

  def isLoaded: Boolean

  def getSections: Array[NMSChunkSection]

  def toCustomChunkFormat: Array[Byte]

}
