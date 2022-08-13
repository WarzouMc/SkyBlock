package fr.warzou.skyblock.adapter.api.common.wrap

/**
 * Make a class wrappable.
 * @tparam A implementation type
 *
 * @version 0.0.1
 * @author Warzou
 */
trait Wrappable[A <: Wrappable[A]] {
  /**
   * @return wrapper for this Wrappable
   */
  def wrapper(): Wrapper[_, A]

  /**
   * @return unwrapper for this Wrappable
   */
  def unwrapper(): Unwrapper[A, _]
}
