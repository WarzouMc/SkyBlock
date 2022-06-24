package fr.warzou.skyblock.adapter.api.world

trait Block {

  def name: String

  def data: Int

  def nbt: Array[Byte]

}
