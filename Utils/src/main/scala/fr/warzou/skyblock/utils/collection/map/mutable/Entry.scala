package fr.warzou.skyblock.utils.collection.map.mutable

case class Entry[K, V](key: K, value: V) {
/*
  def key: K = _key

  def value: V = _value*/

  override def toString: String = s"Entry{key=$key, value=$value}"
}
