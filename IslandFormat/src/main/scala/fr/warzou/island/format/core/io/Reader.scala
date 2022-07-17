package fr.warzou.island.format.core.io

trait Reader[A] {
  def read: A
}
