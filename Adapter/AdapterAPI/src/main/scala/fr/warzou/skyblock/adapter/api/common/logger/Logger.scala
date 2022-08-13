package fr.warzou.skyblock.adapter.api.common.logger

/**
 * A simple logger
 *
 * @version 0.0.1
 * @author Warzou
 */
trait Logger {

  /**
   * Warn a message in output
   * @param message warning message
   */
  def warning(message: String): Unit = log(api = true, Warning(), message)

  /**
   * Send an error message in output
   * @param message severing message
   */
  def error(message: String): Unit = log(api = true, Error(), message)

  /**
   * Info message in output
   * @param message a message
   */
  def log(message: String): Unit = log(api = true, Info(), message)

  /**
   * Warn a message in output
   * @param api api prefix or implementation prefix
   * @param message warning message
   */
  def warning(api: Boolean, message: String): Unit = log(api, Warning(), message)

  /**
   * Send an error message in output
   * @param api api prefix or implementation prefix
   * @param message severing message
   */
  def error(api: Boolean, message: String): Unit = log(api, Error(), message)

  /**
   * Info message in output
   * @param api api prefix or implementation prefix
   * @param message a message
   */
  def log(api: Boolean, message: String): Unit = log(api, Info(), message)

  /**
   * Send message in output
   * @param api api prefix or implementation prefix
   * @param logType message level
   * @param message message to write
   */
  def log(api: Boolean, logType: LogType, message: String): Unit
}
