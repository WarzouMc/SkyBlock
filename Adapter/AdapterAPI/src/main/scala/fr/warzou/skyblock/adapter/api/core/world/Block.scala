package fr.warzou.skyblock.adapter.api.core.world

trait Block {

  def name: String

  def data: Int

  def isBlockEntity: Boolean

  def nbt: Option[Array[Byte]]

}
