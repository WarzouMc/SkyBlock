package fr.warzou.skyblock.adapter.api.world

// TODO: equals (if normal block -> compare type and data else if block entity always false)
trait Block {

  def name: String

  def data: Int

  def isBlockEntity: Boolean

  def nbt: Array[Byte]

}
