package fr.warzou.island.format.writer

class ArrayWriter(private var _array: Array[Byte]) {

  def array: Array[Byte] = _array

  def reduce(length: Int, index: Int): Unit = {
    val old = _array
    _array = new Array[Byte](_array.length - length)
    old.indices.foreach(i =>
      if (i < index) _array(i) = old(i)
      else if (i >= index + length) _array(i - length) = old(i)
    )
  }

  def replaceAndReduce(bytes: Array[Byte], length: Int, index: Int): Unit = {
    val newBytesLength = bytes.length
    (index until index + newBytesLength).foreach(i => _array(i) = bytes(i - index))
    reduce(length - newBytesLength, index + newBytesLength)
  }

  def writeAtIndex(bytes: Array[Byte], index: Int): Unit = {
    space(bytes.length, index)
    (index until index + bytes.length).foreach(i => _array(i) = bytes(i - index))
  }

  def writeAtIndex(byte: Byte, index: Int): Unit = {
    space(1, index)
    _array(index) = byte
  }

  private def space(space: Int, index: Int): Unit = {
    val old = _array
    _array = new Array[Byte](old.length + space)
    old.indices.foreach(i => _array(i + (if (i < index) 0 else space)) = old(i))
  }
}
