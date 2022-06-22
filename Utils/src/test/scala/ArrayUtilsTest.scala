import fr.warzou.skyblock.utils.ArrayUtils
import org.junit.Assert.assertEquals
import org.junit.Test

class ArrayUtilsTest {

  @Test
  def reduceListTest0(): Unit = {
    assertEquals(1 :: 2 :: 3 :: 4 :: 3 :: Nil, ArrayUtils.reduceList(1, 1 :: 2 :: 3 :: 1 :: 4 :: 3 :: Nil))
  }

  @Test
  def reduceListTest1(): Unit = {
    assertEquals(1 :: 2 :: 3 :: 4 :: Nil, ArrayUtils.reduceList(1 :: 2 :: 3 :: 1 :: 4 :: 3 :: Nil))
  }

}
