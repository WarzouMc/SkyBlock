package fr.warzou.island.format.core.common

import org.junit.Assert._
import org.junit.Test

class VersionTest {

  @Test
  def fromTest(): Unit = {
    assertEquals("1.16.5", Version.from("1.16.5_R0.1-SNAPSHOT").toString)
  }

  @Test
  def >(): Unit = {
    assertTrue(Version.from("1.16.5_R0.1-SNAPSHOT") > Version.from("1.16.3_R0.1-SNAPSHOT"))
    assertTrue(Version.from("1.16.5_R0.1-SNAPSHOT") > Version.from("0.16.5_R0.1-SNAPSHOT"))
    assertTrue(Version.from("1.16.5_R0.1-SNAPSHOT") > Version.from("0.15.5_R0.1-SNAPSHOT"))
    assertTrue(Version.from("1.17.5_R0.1-SNAPSHOT") > Version.from("1.16.3_R0.1-SNAPSHOT"))
  }

  @Test
  def <(): Unit = {
    assertTrue(Version.from("1.16.3_R0.1-SNAPSHOT") < Version.from("1.16.5_R0.1-SNAPSHOT"))
    assertTrue(Version.from("0.16.5_R0.1-SNAPSHOT") < Version.from("1.16.5_R0.1-SNAPSHOT"))
    assertTrue(Version.from("0.15.5_R0.1-SNAPSHOT") < Version.from("1.16.5_R0.1-SNAPSHOT"))
    assertTrue(Version.from("1.16.3_R0.1-SNAPSHOT") < Version.from("1.17.5_R0.1-SNAPSHOT"))
  }

  @Test
  def >=(): Unit = {
    assertTrue(Version.from("1.16.5_R0.1-SNAPSHOT") >= Version.from("1.16.3_R0.1-SNAPSHOT"))
    assertTrue(Version.from("1.16.5_R0.1-SNAPSHOT") >= Version.from("0.16.5_R0.1-SNAPSHOT"))
    assertTrue(Version.from("1.16.5_R0.1-SNAPSHOT") >= Version.from("0.15.5_R0.1-SNAPSHOT"))
    assertTrue(Version.from("1.17.5_R0.1-SNAPSHOT") >= Version.from("1.16.3_R0.1-SNAPSHOT"))
    assertTrue(Version.from("1.17.5_R0.1-SNAPSHOT") >= Version.from("1.17.5_R0.1-SNAPSHOT"))
  }

  @Test
  def <=(): Unit = {
    assertTrue(Version.from("1.16.3_R0.1-SNAPSHOT") <= Version.from("1.16.5_R0.1-SNAPSHOT"))
    assertTrue(Version.from("0.16.5_R0.1-SNAPSHOT") <= Version.from("1.16.5_R0.1-SNAPSHOT"))
    assertTrue(Version.from("0.15.5_R0.1-SNAPSHOT") <= Version.from("1.16.5_R0.1-SNAPSHOT"))
    assertTrue(Version.from("1.16.3_R0.1-SNAPSHOT") <= Version.from("1.17.5_R0.1-SNAPSHOT"))
    assertTrue(Version.from("1.17.5_R0.1-SNAPSHOT") <= Version.from("1.17.5_R0.1-SNAPSHOT"))
  }
}
