package fr.warzou.skyblock.utils.collection.map.mutable

import fr.warzou.skyblock.utils.collection.map.mutable.HashBijectiveMap._
import org.jetbrains.annotations.Nullable

import java.util.function.Predicate
import scala.collection.mutable

private object HashBijectiveMap {

  private val defaultLoadFactor: Double = 0.75
  private val defaultCapacity: Int = 32
  private val maxCapacity: Int = 1 << 30

  private def hash(key: Any): Int = if (key == null) 0 else key.## ^ (key.## >>> 16)

  private def tableSizeFor(capacity: Int): Int = 8.max(Math.pow(2, (Math.log(capacity) / Math.log(2)).ceil).toInt).min(maxCapacity)
}

//todo little doc
/**
 * @param capacity
 * @param loadFactor
 * @tparam K keys type
 * @tparam V values type
 *
 */
class HashBijectiveMap[K, V](private var capacity: Int, private val loadFactor: Double) extends BijectiveMap[K, V] {

  private var array: Array[Bucket[K, V]] = new Array[Bucket[K, V]](tableSizeFor(capacity))
  private var contentSize = 0
  private val keySet: mutable.Set[K] = new KeySet
  private val valueSet: mutable.Set[V] = new ValueSet

  capacity = array.length

  def this() = this(defaultCapacity, defaultLoadFactor)

  private def threshold: Int = (capacity * loadFactor).toInt

  override def size: Int = contentSize / 2

  override def isEmpty: Boolean = contentSize == 0

  override def knownSize: Int = contentSize / 2

  override def containsKey(key: K): Boolean = {
    val _hash = hash(key)
    val _index = index(_hash)
    array(_index) != null && array(_index).containKey(key)
  }

  override def containsValue(value: V): Boolean = {
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

  override def keys: mutable.Set[K] = keySet

  override def values: mutable.Set[V] = valueSet

  override def iterator: Iterator[Entry[K, V]] = Itr(array)

  override def put(key: K, value: V): Boolean = {
    if (containsKey(key) || containsValue(value)) return false

    resizeIfRequired()
    insertKey(key, value)
    insertValue(key, value)
    contentSize += 2
    true
  }

  override def putAll(seq: Seq[(K, V)]): Boolean = {
    if (contentSize + seq.size >= capacity && capacity < maxCapacity) resize(tableSizeFor(contentSize + seq.size))
    seq.foldRight(true)((tuple, acc) => acc && put(tuple._1, tuple._2))
  }

  override def setValue(target: K, newValue: V): Unit = {
    if (!containsKey(target)) throw new NoSuchElementException()
    val oldValue = fromKey(target).get
    if (oldValue == newValue) return

    if (containsValue(newValue)) throw new IllegalArgumentException("Target value is already in map !")

    val keyHash = hash(target)
    val keyIndex = index(keyHash)
    val _keyBucket = array(keyIndex).bucketWithKey(target)
    _keyBucket.value = newValue

    val valueHash = hash(oldValue)
    val valueIndex = index(keyHash)
    simplyValueRemove(oldValue)

    insertValue(target, newValue)
  }

  override def setKey(target: V, newKey: K): Unit = {
    if (!containsValue(target)) throw new NoSuchElementException
    val oldKey = fromValue(target).get
    if (oldKey == newKey) return

    if (containsKey(newKey)) throw new IllegalArgumentException("Target key is already in map !")

    val valueHash = hash(target)
    val valueIndex = index(valueHash)
    val _keyBucket = array(valueIndex).bucketWithValue(target)
    _keyBucket.key = newKey

    simplyKeyRemove(oldKey)
    insertKey(newKey, target)
  }

  override def clear(): Unit = {
    contentSize = 0
    array.indices.foreach(array(_) = null)
  }

  override def removeByKey(key: K): Boolean = {
    if (!containsKey(key)) return false
    contentSize -= 2
    simplyValueRemove(fromKey(key).get)
    simplyKeyRemove(key)
  }

  override def removeByValue(value: V): Boolean = {
    if (!containsValue(value)) return false
    contentSize -= 2
    simplyKeyRemove(fromValue(value).get)
    simplyValueRemove(value)
  }

  override def removeByKeyIf(predicate: Predicate[K]): Boolean = {
    val filteredSet = keySet.filter(predicate.test)
    if (filteredSet.isEmpty) return false

    contentSize -= filteredSet.size * 2
    filteredSet.foreach(key => {
      simplyValueRemove(fromKey(key).get)
      simplyKeyRemove(key)
    })
    true
  }

  override def removeByValueIf(predicate: Predicate[V]): Boolean = {
    val filteredSet = valueSet.filter(predicate.test)
    if (filteredSet.isEmpty) return false

    contentSize -= filteredSet.size * 2
    filteredSet.foreach(value => {
      simplyKeyRemove(fromValue(value).get)
      simplyValueRemove(value)
    })
    true
  }

  private def simplyKeyRemove(key: K): Boolean = {
    val _hash = hash(key)
    val _index = index(_hash)
    val bucket = array(_index)

    if (!bucket.hasDefinedNext) {
      array(_index) = null
      return true
    }

    if (bucket.keyToValue && bucket.key == key) {
      array(_index) = bucket.next
      return true
    }

    bucket.removeKeyInNext(key)
    true
  }

  private def simplyValueRemove(value: V): Boolean = {
    val _hash = hash(value)
    val _index = index(_hash)
    val bucket = array(_index)

    if (!bucket.hasDefinedNext) {
      array(_index) = null
      return true
    }

    if (!bucket.keyToValue && bucket.value == value) {
      array(_index) = bucket.next
      return true
    }

    bucket.removeValueInNext(value)
    true
  }

  override def invert: BijectiveMap[V, K] = {
    val invert: HashBijectiveMap[V, K] = new HashBijectiveMap[V, K](capacity, defaultLoadFactor)
    invert.contentSize = contentSize
    foreach(entry => {
      invert.insertKey(entry.value, entry.key)
      invert.insertValue(entry.value, entry.key)
    })
    invert
  }

  private def index(hash: Int): Int = hash & (capacity - 1)

  private def resizeIfRequired(): Unit = {
    if (capacity >= maxCapacity || contentSize / 2 < threshold) return
    resize()
  }

  private def resize(): Unit = resize(capacity * 2)

  private def resize(newCapacity: Int): Unit = {
    val oldArray = array

    capacity = newCapacity
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

  private case class Bucket[K1, V1](keyToValue: Boolean)(val hash: Int, var key: K1, var value: V1, private var _next: Option[Bucket[K1, V1]]) {

    def hasDefinedNext: Boolean = _next.isDefined

    def next: Bucket[K1, V1] = {
      if (!hasDefinedNext) throw new NoSuchElementException
      else _next.get
    }

    def next_=(newValue: Bucket[K1, V1]): Unit = _next = newValue match {
      case null => None
      case _ => Some(newValue)
    }

    def insertVal(bucket: Bucket[K1, V1]): Unit = if (hasDefinedNext) next.insertVal(bucket) else next = bucket

    def removeKeyInNext(_key: K1): Unit = {
      if (next.keyToValue && next.key == _key) {
        _next = next._next
        return
      }
      next.removeKeyInNext(_key)
    }

    def removeValueInNext(_value: V1): Unit = {
      if (!next.keyToValue && next.value == _value) {
        _next = next._next
        return
      }
      next.removeValueInNext(_value)
    }

    def bucketWithKey(_key: K1): Bucket[K1, V1] =
      if (keyToValue && key == _key) this
      else if (hasDefinedNext) next.bucketWithKey(_key)
      else null

    def bucketWithValue(_value: V1): Bucket[K1, V1] =
      if (!keyToValue && value == _value) this
      else if (hasDefinedNext) next.bucketWithValue(_value)
      else null

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

  private abstract class SelfSet[E] extends mutable.AbstractSet[E] with mutable.Set[E] {
    override def size: Int = HashBijectiveMap.this.size

    override def clear(): Unit = HashBijectiveMap.this.clear()

    override def addOne(elem: E): SelfSet.this.type = throw new UnsupportedOperationException()
  }

  private class KeySet extends SelfSet[K] {
    override def contains(elem: K): Boolean =  HashBijectiveMap.this.containsKey(elem)

    override def iterator: Iterator[K] = HashBijectiveMap.this.iterator.map(_.key)

    override def subtractOne(elem: K): KeySet.this.type = {
      HashBijectiveMap.this.removeByKey(elem)
      this
    }
  }

  private class ValueSet extends SelfSet[V] {
    override def contains(elem: V): Boolean =  HashBijectiveMap.this.containsValue(elem)

    override def iterator: Iterator[V] = HashBijectiveMap.this.iterator.map(_.value)

    override def subtractOne(elem: V): ValueSet.this.type = {
      HashBijectiveMap.this.removeByValue(elem)
      this
    }
  }
}
