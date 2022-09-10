package fr.warzou.skyblock.nms.versioning.api.utils.io

case class NibbleArray(override val size: Int) extends Iterable[Nibble] {

  def this(array: Array[Nibble]) = {
    this(array.length)
    array.indices.foreach(i => set(i, array(i)))
  }

  private val array: Array[Nibble] = new Array[Nibble](size)
  fill(Nibble.zero)

  def apply(index: Int): Nibble = array(index)

  def fill(value: Nibble): Unit = array.indices.foreach(set(_, value))

  def set(index: Int, nibble: Nibble): Unit = array(index) = if (nibble == null) Nibble.zero else nibble

  def nibblesToBytes: Array[Byte] = array.map(_.byte)

  override def iterator: Iterator[Nibble] = array.iterator
}

case object NibbleArray {

  def toByteArray(nibbleArray: NibbleArray): Array[Byte] = {
    val nibblesSize = nibbleArray.size
    val byteArray = new Array[Byte](nibblesSize / 2)
    for (i <- 0 until nibblesSize / 2)
      byteArray(i) = if (i * 2 + 1 < nibblesSize) nibbleArray(i * 2) ++ nibbleArray(i * 2 + 1) else nibbleArray(i * 2) ++ Nibble.zero
    byteArray
  }
}
