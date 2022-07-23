package fr.warzou.skyblock.utils.collection.map.mutable

import java.util.function.Predicate
import java.util.stream.Collectors
import scala.collection.mutable
import scala.language.{existentials, postfixOps}

//todo change name...
object BijectiveMap {

  def createHashBijectiveMap[K, V](): BijectiveMap[K, V] = new HashBijectiveMap[K, V]()

}

trait BijectiveMap[K, V] extends Iterable[Entry[K, V]] {

  def containKey(key: K): Boolean

  def containValue(value: V): Boolean

  /**
   * Return optionally value associated with a key.
   * @param key a key in BijectiveMap
   * @return an optionally value associated with `key`, `None` if `key` doesn't exist
   */
  def fromKey(key: K): Option[V]

  /**
   * Return optionally key associated with a value.
   * @param value a value in BijectiveMap
   * @return an optionally key associated with `value`, `None` if `value` doesn't exist
   */
  def fromValue(value: V): Option[K]

  /**
   * Collect all keys of this BijectiveMap
   * @return all keys of BiMap in a [[Set]]
   */
  def keys: mutable.Set[K]

  /**
   * Collect all values of this BijectiveMap
   * @return all values of BiMap in a [[Set]]
   */
  def values: mutable.Set[V]

  /**
   * Inverse keys and values in this BijectiveMap
   * @return return a new BijectiveMap with an inversion of keys and values
   */
  def invert: BijectiveMap[V, K]

  def +(set: (K, V)): Unit = put(set._1, set._2)

  def put(key: K, value: V): Unit

  def putAll(map: BijectiveMap[K, V]): Unit = putAll(map.map(entry => (entry.key, entry.value)).toSeq)

  def putAll(seq: (K, V)*): Unit = putAll(seq)

  def putAll(seq: Seq[(K, V)]): Unit

  def clear(): Unit

  def -(key: K): Unit = removeByKey(key)

  def removeByKey(key: K): Boolean

  def removeByValue(value: V): Boolean

  def removeByKeyIf(predicate: Predicate[K]): Boolean

  def removeByValueIf(predicate: Predicate[V]): Boolean
}