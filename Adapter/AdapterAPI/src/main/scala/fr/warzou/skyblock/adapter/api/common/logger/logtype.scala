package fr.warzou.skyblock.adapter.api.common.logger

sealed trait LogType
final case class Info() extends LogType
final case class Warning() extends LogType
final case class Error() extends LogType
final case class Save() extends LogType