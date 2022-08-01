package fr.warzou.skyblock.utils.collection.map.mutable

import org.junit.Assert._
import org.junit.Test

import java.io.File


//todo
class HashBijectiveMapTest {

  @Test
  def containKeyAndValueTest(): Unit = {
    val bijectiveMap: BijectiveMap[String, Any] = BijectiveMap.createHashBijectiveMap()
    bijectiveMap.put("A owo file", new File("owo/test"))

    assertTrue(bijectiveMap.containKey("A owo file"))
    assertTrue(bijectiveMap.containValue(new File("owo/test")))
  }

  @Test
  def fromKeyTest(): Unit = {
    val bijectiveMap = BijectiveMap.createHashBijectiveMap[String, Int]()
    bijectiveMap.put("a first int", 366738)
    bijectiveMap.put("a second int", 8379)

    assertEquals(None, bijectiveMap.fromKey("none"))
    assertEquals(Some(366738), bijectiveMap.fromKey("a first int"))
    assertEquals(Some(8379), bijectiveMap.fromKey("a second int"))
  }

  @Test
  def fromValueTest(): Unit = {
    val bijectiveMap = BijectiveMap.createHashBijectiveMap[String, Int]()
    bijectiveMap.put("a first int", 366738)
    bijectiveMap.put("a second int", 8379)

    assertEquals(None, bijectiveMap.fromValue(0))
    assertEquals(Some("a first int"), bijectiveMap.fromValue(366738))
    assertEquals(Some("a second int"), bijectiveMap.fromValue(8379))
  }
}
