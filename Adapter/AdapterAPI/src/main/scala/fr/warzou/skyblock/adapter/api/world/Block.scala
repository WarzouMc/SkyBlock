package fr.warzou.skyblock.adapter.api.world

trait Block {

  def name: String

  def data: Int

  def isBlockEntity: Boolean

  def nbt: Option[Array[Byte]]

}
