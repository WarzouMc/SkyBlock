package fr.warzou.skyblock.adapter.api.common.wrap

@FunctionalInterface
trait Unwrapper[A, B] {
  def unwrap(a: A): B
}
