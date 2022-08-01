package fr.warzou.skyblock.adapter.api.core.entity

import fr.warzou.skyblock.adapter.api.AdapterAPI
import fr.warzou.skyblock.adapter.api.common.wrap.{Unwrapper, Wrappable, Wrapper}
import fr.warzou.skyblock.adapter.api.core.world.Location
import fr.warzou.skyblock.utils.cuboid.Cuboid

trait Entity extends Wrappable[Entity] {

  def location: Location

  def name: String

  def nbt: Array[Byte]
}

abstract case class EntityWrapper[A]() extends Wrapper[A, Entity] with Unwrapper[Entity, A] {
  override def wrap(a: A): Entity

  override def unwrap(a: Entity): A

  def toCustom(entity: A): Entity = wrap(entity)

  def fromCustom(entity: Entity): A = unwrap(entity)
}

trait EntitiesGetter {
  def enumerateEntity(adapter: AdapterAPI, cuboid: Cuboid): List[Entity]
}
