package fr.warzou.skyblock.utils.collection.map.mutable

class HashBiMap[K, V](private var capacity: Int) extends BiMap[K, V] {

  def this() = this(16)

  private var entries = new Array[Entry[K, V]](capacity)
  private var size = 0

  def count: Int = size

  override def fromKey(key: K): Option[V] = filter(_.key == key).headOption.map(_.value)

  override def fromValue(value: V): Option[K] = filter(_.value == value).headOption.map(_.key)

  override def keySet: Set[K] = map(_.key).toSet

  override def valueSet: Set[V] = map(_.value).toSet

  override def iterator: Iterator[Entry[K, V]] = new Itr[K, V](entries)

  override def put(key: K, value: V): Unit = ???

  override def invert: BiMap[V, K] = {
    val invert: BiMap[V, K] = BiMap.createBiMap()
    foreach(entry => invert.put(entry.value, entry.key))
    invert
  }

  private class Itr[A, B](array: Array[Entry[A, B]]) extends Iterator[Entry[A, B]] {

    private var position = 0

    override def hasNext: Boolean = position < array.length

    override def next(): Entry[A, B] = {
      if (!hasNext) throw new NoSuchElementException()
      val entry = array(position)
      position += 1
      entry
    }
  }
}
