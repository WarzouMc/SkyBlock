package fr.warzou.skyblock.adapter.api.common.wrap

/**
 * Unwrap the wrappable A to B
 * @tparam A from type
 * @tparam B to type
 *
 * @version 0.0.1
 * @author Warzou
 */
trait Unwrapper[A <: Wrappable[A], B] {

  /**
   * Unwrap an object.
   * @param a object to unwrap
   * @return an unwrap of `a`
   */
  def unwrap(a: A): B
}
