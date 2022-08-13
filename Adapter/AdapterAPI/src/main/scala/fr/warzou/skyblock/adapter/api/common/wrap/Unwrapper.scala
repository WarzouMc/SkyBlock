package fr.warzou.skyblock.adapter.api.common.wrap

trait Unwrapper[A, B] {
  def unwrap(a: A): B
}
