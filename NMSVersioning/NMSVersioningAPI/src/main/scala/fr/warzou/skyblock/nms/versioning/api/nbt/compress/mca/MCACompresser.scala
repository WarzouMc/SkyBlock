package fr.warzou.skyblock.nms.versioning.api.nbt.compress.mca

trait MCACompresser {

  def defaultCompress: Array[Byte]

  def compress: Array[Byte]

  def notCompress: Array[Byte]
}
