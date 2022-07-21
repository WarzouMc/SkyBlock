package fr.warzou.skyblock.utils.collection.map.mutable

import scala.language.{existentials, postfixOps}

//todo change name...
object BijectiveMap {

  def createHashBijectiveMap[K, V](): BijectiveMap[K, V] = new HashBijectiveMap[K, V]()

}

trait BijectiveMap[K, V] extends Iterable[Entry[K, V]] {

  def existKey(key: K): Boolean

  def existValue(value: V): Boolean

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
  def keys: Set[K]

  /**
   * Collect all values of this BijectiveMap
   * @return all values of BiMap in a [[Set]]
   */
  def values: Set[V]

  /**
   * Inverse keys and values in this BijectiveMap
   * @return return a new BijectiveMap with an inversion of keys and values
   */
  def invert: BijectiveMap[V, K]

  def +(set: (K, V)): Unit = put(set._1, set._2)

  def put(key: K, value: V): Unit
}