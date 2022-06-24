package fr.warzou.skyblock.utils

import scala.collection.IterableFactory

object ArrayUtils {

  /**
   * @param list any list
   * @tparam A list type
   * @return same list who contain only different element
   */
  def reduceList[A](list: List[A]): List[A] = list.foldRight(list)((value, acc) => reduceList(value, acc))

  /**
   * @param element element to reduce
   * @param list any list
   * @tparam A list type
   * @return same list who contain only one <pre>element</pre>
   */
  def reduceList[A](element: A, list: List[A]): List[A] =
    list.foldLeft(List[A]())((acc, value) =>
      if (value != element) value :: acc
      else if (acc.contains(element)) acc
      else value :: acc
    ).reverse
}
