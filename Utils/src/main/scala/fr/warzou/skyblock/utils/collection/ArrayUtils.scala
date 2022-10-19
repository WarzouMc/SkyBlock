package fr.warzou.skyblock.utils.collection

import scala.annotation.tailrec

case object ArrayUtils {

  /**
   * @param list any list
   * @tparam A list type
   * @return same list who contain only different element
   */
  def reduceList[A](list: List[A]): List[A] = list.foldRight(list)((value, acc) => reduceList(value, acc))

  /**
   * @param element element to reduce
   * @param list    any list
   * @tparam A list type
   * @return same list who contain only one <pre>element</pre>
   */
  def reduceList[A](element: A, list: List[A]): List[A] =
    list.foldLeft(List[A]())((acc, value) =>
      if (value != element) value :: acc
      else if (acc.contains(element)) acc
      else value :: acc
    ).reverse

  @tailrec
  def indexOf[A](iterator: Iterator[A], element: A, n: Int = 0): Int = {
    if (!iterator.hasNext) -1
    else if (iterator.next() == element) n
    else indexOf(iterator, element, n + 1)
  }

  def growArray[A](array: Array[A], grow: Int): Array[A] = array ++ Array[A](grow)
}
