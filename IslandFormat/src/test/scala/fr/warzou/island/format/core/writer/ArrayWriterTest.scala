package fr.warzou.island.format.core.writer

import fr.warzou.island.format.writer.ArrayWriter
import org.junit.Test
import org.junit.Assert._

class ArrayWriterTest {

  @Test
  def reduceTest(): Unit = {
    val array = new Array[Byte](6)
    array(0) = 0
    array(1) = 1
    array(2) = 2
    array(3) = 3
    array(4) = 4
    array(5) = 5

    val writer = new ArrayWriter(array)
    writer.reduce(3, 1)

    val expect = new Array[Byte](3)
    expect(0) = 0
    expect(1) = 4
    expect(2) = 5
    assertArrayEquals(expect, writer.array)
  }

  @Test
  def replaceAndReduceTest(): Unit = {
    val array = new Array[Byte](7)
    array(0) = 0
    array(1) = 1
    array(2) = 2
    array(3) = 3
    array(4) = 4
    array(5) = 5
    array(6) = 6

    val newArray = new Array[Byte](3)
    newArray(0) = 6
    newArray(1) = 7
    newArray(2) = 8
    val writer = new ArrayWriter(array)
    writer.replaceAndReduce(newArray, 4, 2)
    val expect = new Array[Byte](6)
    expect(0) = 0
    expect(1) = 1
    expect(2) = 6
    expect(3) = 7
    expect(4) = 8
    expect(5) = 6
    assertArrayEquals(expect, writer.array)
  }

}
