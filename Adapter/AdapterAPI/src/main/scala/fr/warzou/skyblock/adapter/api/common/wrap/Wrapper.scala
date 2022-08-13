package fr.warzou.skyblock.adapter.api.common.wrap

/**
 * Wrap a type A to a Wrappable class B
 * @tparam A from type
 * @tparam B to type
 *
 * @version 0.0.1
 * @author Warzou
 */
trait Wrapper[A, B <: Wrappable[B]] {

  /**
   * Wrap an object.s
   * @param a object to wrap
   * @return a wrap of `a`
   */
  def wrap(a: A): B
}