package fr.warzou.island.format.writer

import fr.warzou.island.format.writer.file.Writer
import org.junit.Test
import org.junit.Assert._

class WriterTest {

  @Test
  def reduceListTest0(): Unit = {
    assertEquals(1 :: 2 :: 3 :: 4 :: 3 :: Nil, Writer.reduceList(1, 1 :: 2 :: 3 :: 1 :: 4 :: 3 :: Nil))
  }

  @Test
  def reduceListTest1(): Unit = {
    assertEquals(1 :: 2 :: 3 :: 4 :: Nil, Writer.reduceList(1 :: 2 :: 3 :: 1 :: 4 :: 3 :: Nil))
  }

}
