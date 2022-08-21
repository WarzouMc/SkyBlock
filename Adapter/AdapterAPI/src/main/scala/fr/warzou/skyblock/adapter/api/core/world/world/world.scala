package fr.warzou.skyblock.adapter.api.core.world.world

import fr.warzou.skyblock.adapter.api.common.wrap.{Unwrapper, Wrappable, Wrapper}

trait World extends Wrappable[World] {

  def levelName: String

  def dimCount: Int

  def dimensions: List[Dimension]

}


abstract class WorldWrapper[A] extends Wrapper[A, World] with Unwrapper[World, A]