package fr.warzou.skyblock.adapter.api.common.wrap

trait Wrapper[A, B] {
  def wrap(a: A): B
}