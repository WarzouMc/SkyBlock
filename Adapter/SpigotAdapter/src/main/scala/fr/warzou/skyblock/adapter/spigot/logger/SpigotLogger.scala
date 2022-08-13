package fr.warzou.skyblock.adapter.spigot.logger

import fr.warzou.skyblock.adapter.api.common.logger
import fr.warzou.skyblock.adapter.api.common.logger.{LogType, Logger}
import fr.warzou.skyblock.adapter.spigot.logger.SpigotLogger.{apiPrefix, spigotPrefix}
import org.bukkit.{Bukkit, ChatColor}

import java.util.logging.Level

class SpigotLogger extends Logger {

  private val _logger = Bukkit.getLogger

  override def log(api: Boolean, logType: LogType, message: String): Unit =
    Bukkit.getLogger.log(level(logType), s"${if (api) apiPrefix else spigotPrefix} $message")

  private def level(logType: LogType): Level = logType match {
    case logger.Error() => Level.SEVERE
    case logger.Warning() => Level.WARNING
    case logger.Info() => Level.INFO
  }
}

object SpigotLogger {
  val apiPrefix = "[SkyBlock]"
  val spigotPrefix = "[SpigotSkyBlock]"
}
