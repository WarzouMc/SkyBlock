package fr.warzou.skyblock.utils

case object TickUtils {

  val tickPerSeconds = 20
  val millisPerTick: Int = 1000 / 20

  def millisToTick(millis: Long): Long = millis / millisPerTick

  def tickToSeconds(tick: Long): Long = tick / tickPerSeconds

}
