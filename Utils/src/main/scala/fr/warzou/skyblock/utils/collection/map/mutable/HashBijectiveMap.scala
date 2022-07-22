package fr.warzou.skyblock.utils.collection.map.mutable

import fr.warzou.skyblock.utils.collection.map.AlreadyPresentException
import fr.warzou.skyblock.utils.collection.map.mutable.HashBijectiveMap._
import org.jetbrains.annotations.Nullable

import scala.collection.mutable

class HashBijectiveMap[K, V](private var capacity: Int, private val loadFactor: Double) extends BijectiveMap[K, V] {

  private var array: Array[Bucket[K, V]] = new Array[Bucket[K, V]](tableSizeFor(capacity))
  private def threshold: Int = (capacity * loadFactor).toInt
  private var contentSize = 0

  capacity = array.length

  def this() = this(defaultCapacity, defaultLoadFactor)

  override def size: Int = contentSize

  override def existKey(key: K): Boolean = {
    val _hash = hash(key)
    val _index = index(_hash)
    array(_index) != null && array(_index).containKey(key)
  }

  override def existValue(value: V): Boolean = {
    val _hash = hash(value)
    val _index = index(_hash)
    array(_index) != null && array(_index).containValue(value)
  }

  override def fromKey(key: K): Option[V] = {
    val _hash = hash(key)
    val _index = index(_hash)
    if (array(_index) == null) None else array(_index).getValue(key)
  }

  override def fromValue(value: V): Option[K] = {
    val _hash = hash(value)
    val _index = index(_hash)
    if (array(_index) == null) None else array(_index).getKey(value)
  }

  override def keys: Set[K] = {
    val set: mutable.Set[K] = new mutable.HashSet[K](capacity, mutable.HashSet.defaultLoadFactor)
    for (bucket <- array) {
      if (bucket.keyToValue) set += bucket.key
      var _bucket = bucket
      while (_bucket.hasDefinedNext) {
        _bucket = _bucket.next
        if (_bucket.keyToValue) set += _bucket.key
      }
    }
    Set.from(set)
  }

  override def values: Set[V] = {
    val set: mutable.Set[V] = new mutable.HashSet[V](capacity, mutable.HashSet.defaultLoadFactor)
    for (bucket <- array) {
      if (!bucket.keyToValue) set += bucket.value
      var _bucket = bucket
      while (_bucket.hasDefinedNext) {
        _bucket = _bucket.next
        if (!_bucket.keyToValue) set += _bucket.value
      }
    }
    Set.from(set)
  }

  override def iterator: Iterator[Entry[K, V]] = Itr(array)

  override def put(key: K, value: V): Unit = {
    if (existKey(key)) throw new AlreadyPresentException("Key already existe in this map !")
    if (existValue(value)) throw new AlreadyPresentException("Value already existe in this map !")

    resizeIfRequired()
    insertKey(key, value)
    insertValue(key, value)
  }

  //todo optimize
  override def invert: BijectiveMap[V, K] = {
    val invert: BijectiveMap[V, K] = BijectiveMap.createHashBijectiveMap()
    foreach(entry => invert.put(entry.value, entry.key))
    invert
  }

  private def index(hash: Int): Int = hash & (capacity - 1)

  private def resizeIfRequired(): Unit = {
    if (capacity >= maxCapacity || contentSize < threshold) return
    resize()
  }

  private def resize(): Unit = {
    val oldThreshold = threshold
    val oldCapacity = capacity
    val oldArray = array

    capacity = oldCapacity * 2
    array = new Array[Bucket[K, V]](capacity)
    for (oldBucket <- oldArray) {
      var _oldBucket = oldBucket
      var key = _oldBucket.key
      var value = _oldBucket.value
      insertKey(key, value)
      insertValue(key, value)

      while(_oldBucket.hasDefinedNext) {
        _oldBucket = _oldBucket.next
        key = _oldBucket.key
        value = _oldBucket.value
        insertKey(key, value)
        insertValue(key, value)
      }
    }
  }

  private def insertKey(key: K, value: V): Unit = {
    val _hash = hash(key)
    val _index = index(_hash)
    val bucket = keyBucket(_hash, key, value)
    insertBucket(bucket, _index)
  }

  private def insertValue(key: K, value: V): Unit = {
    val _hash = hash(value)
    val _index = index(_hash)
    val bucket = valueBucket(_hash, key, value)
    insertBucket(bucket, _index)
  }

  private def insertBucket(bucket: Bucket[K, V], _index: Int): Unit = {
    if (array(_index) == null) array(_index) = bucket
    else array(_index).insertVal(bucket)
  }

  private def keyBucket(hash: Int, key: K, value: V): Bucket[K, V] = new Bucket[K, V](true)(hash, key, value, None)

  private def valueBucket(hash: Int, key: K, value: V): Bucket[K, V] = new Bucket[K, V](false)(hash, key, value, None)

  override def toString(): String = {
    val builder: mutable.StringBuilder = new mutable.StringBuilder("BijectiveMap{")
    val iterator = this.iterator
    if (!iterator.hasNext) return builder.append('}').toString

    while (iterator.hasNext) {
      val entry = iterator.next()
      val key: Any = entry.key
      val value: Any = entry.value
      if (key == this) builder.append("(this map)") else builder.append(entry.key)
      builder.append("=")
      if (value == this) builder.append("(this map)") else builder.append(entry.value)

      if (iterator.hasNext) builder.append(", ")
    }
    builder.append("}").toString
  }

  override def equals(obj: Any): Boolean = {
    if (obj == null || !obj.isInstanceOf[HashBijectiveMap[_, _]]) return false
    val map = obj.asInstanceOf[HashBijectiveMap[_, _]]
    if (contentSize != map.contentSize || array.length != map.array.length) return false
    array.indices.foldRight(true)((i, acc) => acc && map.array(i) == array(i))
  }

  private case class Bucket[K1, V1](keyToValue: Boolean)(val hash: Int, val key: K1, val value: V1, private var _next: Option[Bucket[K1, V1]]) {

    def hasDefinedNext: Boolean = _next.isDefined

    def next: Bucket[K1, V1] = {
      if (!hasDefinedNext) throw new NoSuchElementException()
      else _next.get
    }

    def next_=(newValue: Bucket[K1, V1]): Unit = _next = newValue match {
      case null => None
      case _ => Some(newValue)
    }

    def insertVal(bucket: Bucket[K1, V1]): Unit = if (hasDefinedNext) next.insertVal(bucket) else next = bucket

    def containKey(key: K1): Boolean = if (keyToValue && key == this.key) true else hasDefinedNext && next.containKey(key)

    def containValue(value: V1): Boolean = if (!keyToValue && value == this.value) true else hasDefinedNext && next.containValue(value)

    def getValue(key: K1): Option[V1] = if (keyToValue && key == this.key) Some(value) else if (hasDefinedNext) next.getValue(key) else None

    def getKey(value: V1): Option[K1] = if (!keyToValue && value == this.value) Some(key) else if (hasDefinedNext) next.getKey(value) else None

    override def toString: String = s"Bucket{keyToValue=$keyToValue, hash=$hash, key=$key, value=$value, next=${_next}}"
  }

  private case class Itr(array: Array[Bucket[K, V]]) extends Iterator[Entry[K, V]] {

    private var position: Int = 0
    private var currentBucket: Option[Bucket[K, V]] = None

    override def hasNext: Boolean = {
      if (currentBucket.isDefined && hasNextInBucket(currentBucket.get)) return true
      if (position >= array.length) return false
      var _position = position
      while (_position < array.length) {
        val bucket: Bucket[K, V] = array(_position)
        _position += 1
        if (bucket != null && (bucket.keyToValue || hasNextInBucket(bucket))) return true
      }
      false
    }

    private def hasNextInBucket(bucket: Bucket[K, V]): Boolean = {
      var _bucket = bucket
      while (_bucket.hasDefinedNext) {
        _bucket = _bucket.next
        if (_bucket.keyToValue) return true
      }
      false
    }

    override def next(): Entry[K, V] = {
      if (currentBucket.isDefined) {
        val result: Bucket[K, V] = nextInBucket(currentBucket.get)
        if (result != null) return applyBucket(result)
      }
      if (position >= array.length) throw new ArrayIndexOutOfBoundsException()
      while (position < array.length) {
        var bucket: Bucket[K, V] = array(position)
        position += 1
        if (bucket != null) {
          if (bucket.keyToValue) return applyBucket(bucket)
          bucket = nextInBucket(bucket)
          if (bucket != null) return applyBucket(bucket)
        }
      }
      throw new NoSuchElementException()
    }

    @Nullable
    private def nextInBucket(bucket: Bucket[K, V]): Bucket[K, V] = {
      var _bucket = bucket
      while (_bucket.hasDefinedNext) {
        _bucket = _bucket.next
        if (_bucket.keyToValue) return _bucket
      }
      null
    }

    private def applyBucket(bucket: Bucket[K, V]): Entry[K, V] = {
      currentBucket = Some(bucket)
      bucketToEntry(bucket)
    }

    private def bucketToEntry(bucket: Bucket[K, V]): Entry[K, V] = Entry(bucket.key, bucket.value)
  }
}

private object HashBijectiveMap {

  private val defaultLoadFactor: Double = 0.75
  private val defaultCapacity: Int = 16
  private val maxCapacity: Int = 1 << 30

  private def hash(key: Any): Int = if (key == null) 0 else key.## ^ (key.## >>> 16)

  private def tableSizeFor(capacity: Int): Int = 4.max(Math.pow(2, (Math.log(capacity) / Math.log(2)).ceil).toInt).min(maxCapacity)
}
