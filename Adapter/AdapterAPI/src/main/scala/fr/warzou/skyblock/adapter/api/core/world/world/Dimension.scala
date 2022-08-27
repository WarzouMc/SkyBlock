package fr.warzou.skyblock.adapter.api.core.world.world

trait Dimension {

  def world: World

  def isCustom: Boolean

  def id: Int

  def name: String

  def regionCount: Int

  def regions(): List[Region]
}
