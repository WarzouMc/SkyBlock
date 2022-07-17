package fr.warzou.skyblock.utils.collection.map.mutable

abstract class Entry[K, V](var key: K, var value: V) {

  def key_=(_key: K): Unit

  def value_=(_value: V): Unit

  override def equals(obj: Any): Boolean = {
    if (obj == null || !obj.isInstanceOf[Entry]) return false
    val entry = obj.asInstanceOf[Entry]
    entry.key == key && entry.value == value
  }

  override def toString: String = s"Entry{key=$key, value=$value}"
}
