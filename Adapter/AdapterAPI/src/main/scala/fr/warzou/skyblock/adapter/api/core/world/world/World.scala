package fr.warzou.skyblock.adapter.api.core.world.world

import java.io.File

trait World {

  def name: String

  def level: File

  def dimCount: Int = dimensions.length

  def dimensions: List[_ <: Dimension]

  def load(): Unit
}