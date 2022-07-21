package fr.warzou.skyblock.utils.collection.map.mutable

abstract case class Entry[K, V](key: K, value: V) {
  override def toString: String = s"Entry{key=$key, value=$value}"
}
