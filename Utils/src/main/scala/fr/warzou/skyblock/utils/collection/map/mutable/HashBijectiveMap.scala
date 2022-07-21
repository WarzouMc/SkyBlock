package fr.warzou.skyblock.utils.collection.map.mutable

import fr.warzou.skyblock.utils.collection.map.mutable.HashBijectiveMap._

import scala.collection.mutable

case class HashBijectiveMap[K, V](private var capacity: Int, private val loadFactor: Double) extends BijectiveMap[K, V] {

  private var array: Array[Bucket[K, V]] = new Array[Bucket[K, V]](tableSizeFor(capacity))
  private def threshold: Int = (capacity * loadFactor).toInt
  private var contentSize = 0

  capacity = array.length

  def this() = this(defaultCapacity, defaultLoadFactor)

  override def size: Int = contentSize

  override def existKey(key: K): Boolean = ???

  override def existValue(value: V): Boolean = ???

  override def fromKey(key: K): Option[V] = filter(_.key == key).headOption.map(_.value)

  override def fromValue(value: V): Option[K] = filter(_.value == value).headOption.map(_.key)

  override def keys: Set[K] = map(_.key).toSet

  override def values: Set[V] = map(_.value).toSet

  override def iterator: Iterator[Entry[K, V]] = ???

  override def put(key: K, value: V): Unit = {
    val _hash = hash(key)
    val _index = index(_hash)
    resizeIfRequired()
  }

  override def invert: BijectiveMap[V, K] = {
    val invert: BijectiveMap[V, K] = BijectiveMap.createHashBijectiveMap()
    foreach(entry => invert.put(entry.value, entry.key))
    invert
  }

  private def index(hash: Int): Int = hash & (array.length - 1)

  private def resizeIfRequired(): Unit = {
    if (contentSize < threshold || capacity >= maxCapacity) return
    resize()
  }

  private def resize(): Unit = {
    val oldThreshold = threshold
    val oldCapacity = capacity
    val oldArray = array

    val newCapacity = oldCapacity * 2
    array = new Array[Bucket[K, V]](newCapacity)

    //todo remap
  }

  private case class Bucket[K1, V1](override val key: K1, override val value: V1, hash: Int, private var _next: Option[Bucket[K1, V1]]) extends Entry[K1, V1](key, value) {

    def hasDefinedNext: Boolean = _next.isDefined

    def next: Option[Bucket[K1, V1]] = _next

    def next_=(newValue: Bucket[K1, V1]): Unit = _next = newValue match {
      case null => None
      case _ => Some(newValue)
    }
  }
}

private object HashBijectiveMap {

  private val defaultLoadFactor: Double = 0.75
  private val defaultCapacity: Int = 16
  private val maxCapacity: Int = 1 << 30

  private def hash(key: Any): Int = if (key == null) 0 else key.## ^ (key.## >>> 16)

  private def tableSizeFor(capacity: Int): Int = 4.max(Math.pow(2, (Math.log(capacity) / Math.log(2)).ceil).toInt).min(maxCapacity)
}
