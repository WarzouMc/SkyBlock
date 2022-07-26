package fr.warzou.skyblock.adapter.api.common.wrap

@FunctionalInterface
trait Wrapper[A, B] {
  def wrap(a: A): B
}