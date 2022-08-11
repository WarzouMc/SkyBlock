package fr.warzou.skyblock.adapter.api.common.logger

trait Logger {

  def warning(message: String): Unit = log(Warning(), message)

  def error(message: String): Unit = log(Error(), message)

  def save(message: String): Unit = log(Save(), message)

  def log(message: String): Unit = log(Info(), message)

  def log(logType: LogType, message: String): Unit

  def rawLog(message: String): Unit

}
