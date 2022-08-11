package fr.warzou.skyblock.adapter.api.common.logger

trait Logger {

  def warning(message: String): Unit = log(api = true, Warning(), message)

  def error(message: String): Unit = log(api = true, Error(), message)

  def save(message: String): Unit = log(api = true, Save(), message)

  def log(message: String): Unit = log(api = true, Info(), message)

  def warning(api: Boolean, message: String): Unit = log(api, Warning(), message)

  def error(api: Boolean, message: String): Unit = log(api, Error(), message)

  def save(api: Boolean, message: String): Unit = log(api, Save(), message)

  def log(api: Boolean, message: String): Unit = log(api, Info(), message)

  def log(api: Boolean, logType: LogType, message: String): Unit
}
