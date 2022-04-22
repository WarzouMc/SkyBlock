package fr.warzou.island.format.writer

import org.junit.Test
import org.junit.Assert._

import java.io.File
import fr.warzou.island.format.writer.file.Writer

class WriterTest {

  @Test
  def writeTest(): Unit = {
    val writer = new Writer(new File(Thread.currentThread().getContextClassLoader.getResource("test.island").getFile))
    writer.write(null)
    assertTrue(true)
    // TODO: test
  }

}
