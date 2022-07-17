package fr.warzou.skyblock.utils.collection.map.mutable

import scala.language.postfixOps

//todo change name...
object BiMap {

  def createBiMap[K, V](): BiMap[K, V] = new HashBiMap[K, V]()

}

trait BiMap[K, V] extends Iterable[Entry[K, V]] {

  def count: Int

  def fromKey(key: K): Option[V]

  def fromValue(value: V): Option[K]

  def keySet: Set[K]

  def valueSet: Set[V]

  def invert: BiMap[V, K]

  def +(set: (K, V)): Unit = put(set._1, set._2)

  def put(key: K, value: V): Unit
}