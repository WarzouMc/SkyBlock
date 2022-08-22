package fr.warzou.skyblock.adapter.api.core.world.world

import fr.warzou.skyblock.adapter.api.common.wrap.{Unwrapper, Wrappable, Wrapper}

sealed trait EnvironmentType
case object Overworld extends EnvironmentType
case object Nether extends EnvironmentType
case object End extends EnvironmentType

case object EnvironmentType {
  def idOf(worldType: EnvironmentType): Int = worldType match {
    case Overworld => 0
    case Nether => -1
    case End => 1
  }

  def fromId(id: Int): EnvironmentType = id match {
    case 0 => Overworld
    case 1 => Nether
    case 2 => End
    case _ => throw new IllegalArgumentException(s"Unknown world type id $id !")
  }
}

trait World {

  def id: Int

  def worldEnvironmentId: Int

  def regionCount: Int = regions.length

  def regions: List[Region]
}