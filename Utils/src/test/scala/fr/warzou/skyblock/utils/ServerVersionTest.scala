package fr.warzou.skyblock.utils

import org.junit.Assert._
import org.junit.Test

class ServerVersionTest {

  @Test
  def fromTest(): Unit = {
    assertEquals("1.16.5", ServerVersion.from("1.16.5_R0.1-SNAPSHOT").toString)
  }

  @Test
  def >(): Unit = {
    assertTrue(ServerVersion.from("1.16.5_R0.1-SNAPSHOT") > ServerVersion.from("1.16.3_R0.1-SNAPSHOT"))
    assertTrue(ServerVersion.from("1.16.5_R0.1-SNAPSHOT") > ServerVersion.from("0.16.5_R0.1-SNAPSHOT"))
    assertTrue(ServerVersion.from("1.16.5_R0.1-SNAPSHOT") > ServerVersion.from("0.15.5_R0.1-SNAPSHOT"))
    assertTrue(ServerVersion.from("1.17.5_R0.1-SNAPSHOT") > ServerVersion.from("1.16.3_R0.1-SNAPSHOT"))
  }

  @Test
  def <(): Unit = {
    assertTrue(ServerVersion.from("1.16.3_R0.1-SNAPSHOT") < ServerVersion.from("1.16.5_R0.1-SNAPSHOT"))
    assertTrue(ServerVersion.from("0.16.5_R0.1-SNAPSHOT") < ServerVersion.from("1.16.5_R0.1-SNAPSHOT"))
    assertTrue(ServerVersion.from("0.15.5_R0.1-SNAPSHOT") < ServerVersion.from("1.16.5_R0.1-SNAPSHOT"))
    assertTrue(ServerVersion.from("1.16.3_R0.1-SNAPSHOT") < ServerVersion.from("1.17.5_R0.1-SNAPSHOT"))
  }

  @Test
  def >=(): Unit = {
    assertTrue(ServerVersion.from("1.16.5_R0.1-SNAPSHOT") >= ServerVersion.from("1.16.3_R0.1-SNAPSHOT"))
    assertTrue(ServerVersion.from("1.16.5_R0.1-SNAPSHOT") >= ServerVersion.from("0.16.5_R0.1-SNAPSHOT"))
    assertTrue(ServerVersion.from("1.16.5_R0.1-SNAPSHOT") >= ServerVersion.from("0.15.5_R0.1-SNAPSHOT"))
    assertTrue(ServerVersion.from("1.17.5_R0.1-SNAPSHOT") >= ServerVersion.from("1.16.3_R0.1-SNAPSHOT"))
    assertTrue(ServerVersion.from("1.17.5_R0.1-SNAPSHOT") >= ServerVersion.from("1.17.5_R0.1-SNAPSHOT"))
  }

  @Test
  def <=(): Unit = {
    assertTrue(ServerVersion.from("1.16.3_R0.1-SNAPSHOT") <= ServerVersion.from("1.16.5_R0.1-SNAPSHOT"))
    assertTrue(ServerVersion.from("0.16.5_R0.1-SNAPSHOT") <= ServerVersion.from("1.16.5_R0.1-SNAPSHOT"))
    assertTrue(ServerVersion.from("0.15.5_R0.1-SNAPSHOT") <= ServerVersion.from("1.16.5_R0.1-SNAPSHOT"))
    assertTrue(ServerVersion.from("1.16.3_R0.1-SNAPSHOT") <= ServerVersion.from("1.17.5_R0.1-SNAPSHOT"))
    assertTrue(ServerVersion.from("1.17.5_R0.1-SNAPSHOT") <= ServerVersion.from("1.17.5_R0.1-SNAPSHOT"))
  }
}
