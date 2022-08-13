package fr.warzou.skyblock.adapter.api.common.logger

/**
 * Some log types
 *
 * @version 0.0.1s
 * @author Warzou
 */
sealed trait LogType
final case class Info() extends LogType
final case class Warning() extends LogType
final case class Error() extends LogType