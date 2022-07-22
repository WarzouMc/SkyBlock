package fr.warzou.skyblock.utils.collection.map.mutable

import org.junit.Test

import java.io.File
//todo
class HashBijectiveMapTest {

  @Test
  def putTest(): Unit = {
    val bijectiveMap: BijectiveMap[String, Any] = BijectiveMap.createHashBijectiveMap()
    println(bijectiveMap)
    bijectiveMap.put("A owo file", new File("owo/test"))
    bijectiveMap.put("Another owo file", new File("owo/test2"))
    bijectiveMap.put("yep more", new File("owo1/test2"))
    bijectiveMap.put("oof", bijectiveMap)
    println(bijectiveMap)
  }

}
