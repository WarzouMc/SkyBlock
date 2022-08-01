package fr.warzou.skyblock.utils.collection.map.mutable

import java.util.function.Predicate
import scala.collection.mutable
import scala.language.{existentials, postfixOps}

case object BijectiveMap {
  def createHashBijectiveMap[K, V](): BijectiveMap[K, V] = new HashBijectiveMap[K, V]()
}

/**
 * Base type for BijectiveMap
 *
 * A BijectiveMap is like a normal [[mutable.Map]].
 * The difference is in the fact of a value is unique.
 *
 * This constraint allow to have the method invert.
 *
 *
 * @tparam K keys type
 * @tparam V values type
 *
 * @version 0.0.1
 * @since 0.0.1
 * @author Warzou
 */
trait BijectiveMap[K, V] extends Iterable[Entry[K, V]] {

  /**
   * Check if BijectiveMap contain `key`
   * @param key a object `K`
   * @return this BijectiveMap contain `key`
   */
  def containKey(key: K): Boolean

  /**
   * Check if BijectiveMap contain `value`
   * @param value a object `V`
   * @return this BijectiveMap contain `value`
   */
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
   * Create a new BijectiveMap who inverse this map keys and values
   * @return return a new BijectiveMap with an inversion of keys and values
   */
  def invert: BijectiveMap[V, K]

  /**
   * Add a key value paire in map
   * @param key a key
   * @param value a value
   * @return if key value paire was correctly added
   */
  def put(key: K, value: V): Boolean

  /**
   * Put all elements of another BijectiveMap in this BijectiveMap
   * @param map another BijectiveMap
   * @return if all elements of `map` was correctly added
   */
  def putAll(map: BijectiveMap[K, V]): Boolean = putAll(map.map(entry => (entry.key, entry.value)).toSeq)

  /**
   * Put every elements of a [[Seq]] in this BijectiveMap
   * @param seq a sequence of key value paires
   * @return if all elements of `seq` was correctly added
   */
  //def putAll(seq: (K, V)*): Boolean = putAll(seq)

  /**
   * Put every elements of a [[Seq]] in this BijectiveMap
   * @param seq a sequence of key value paires
   * @return if all elements of `seq` was correctly added
   */
  def putAll(seq: Seq[(K, V)]): Boolean

  /**
   * Make this BijectiveMap empty
   */
  def clear(): Unit

  /**
   * Remove a key value paire into this BijectiveMap from a key
   * @param key a key in map
   * @return if key value paire was correctly removed
   */
  def removeByKey(key: K): Boolean

  /**
   * Remove a value key paire into this BijectiveMap from a value
   * @param value a value in map
   * @return if value key paire was correctly removed
   */
  def removeByValue(value: V): Boolean

  /**
   * Delete all key value paires who pass the test
   * @param predicate condition to delete a key value paire
   * @return true if some key value paires was delete
   */
  def removeByKeyIf(predicate: Predicate[K]): Boolean

  /**
   * Delete all value key paires who pass the test
   * @param predicate condition to delete a value key paire
   * @return true if some value key paires was delete
   */
  def removeByValueIf(predicate: Predicate[V]): Boolean

  /**
   * Delete all key value paires who not pass the test
   * @param predicate condition to not delete a key value paire
   * @return true if some key value paires was delete
   */
  def removeByKeyIfNot(predicate: Predicate[K]): Boolean = removeByKeyIf(predicate.negate())

  /**
   * Delete all value key paires who not pass the test
   * @param predicate condition to not delete a value key paire
   * @return true if some value key paires was delete
   */
  def removeByValueIfNot(predicate: Predicate[V]): Boolean = removeByValueIf(predicate.negate())
}