package fr.warzou.skyblock.adapter.api.common.wrap

trait Wrappable[A] {

  def wrapper: Wrapper[_, A]

  def unwrapper: Unwrapper[A, _]
}
