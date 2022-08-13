package fr.warzou.skyblock.adapter.api.core.entity

import fr.warzou.skyblock.adapter.api.common.wrap.{Unwrapper, Wrappable, Wrapper}
import fr.warzou.skyblock.adapter.api.core.world.Location
import fr.warzou.skyblock.utils.cuboid.Cuboid

/**
 * A wrap of minecraft entity.
 *
 * @version 0.0.1
 * @author Warzou
 */
trait Entity extends Wrappable[Entity] {

  /**
   * @return entity location
   */
  def location: Location

  /**
   * @return namespace ":" type
   */
  def name: String

  /**
   * @return NNBTTagCompound, under a byte array, shape who represent this entity
   */
  def nbt: Array[Byte]
}

/**
 * Wrapper for minecraft entity to [[Entity]] and [[Entity]] to minecraft entity.
 *
 * @tparam A minecraft entity type
 * @version 0.0.1
 * @author Warzou
 */
abstract class EntityWrapper[A]() extends Wrapper[A, Entity] with Unwrapper[Entity, A]

/**
 * A class to create a lambda function to enumerate all entities in a cuboid.
 *
 * @version 0.0.1
 * @author Warzou
 */
trait EntitiesGetter {
  /**
   * Create a list of all entities in a cuboid.
   * @param cuboid a cuboid
   * @return a list of all entities in target cuboid
   */
  def enumerateEntity(cuboid: Cuboid): List[Entity]
}
