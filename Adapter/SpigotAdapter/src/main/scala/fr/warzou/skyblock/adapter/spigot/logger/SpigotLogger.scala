package fr.warzou.skyblock.adapter.spigot.logger

import fr.warzou.skyblock.adapter.api.common.logger
import fr.warzou.skyblock.adapter.api.common.logger.{LogType, Logger}
import fr.warzou.skyblock.adapter.spigot.logger.SpigotLogger.{apiPrefix, spigotPrefix}
import org.bukkit.{Bukkit, ChatColor}

import java.util.logging.Level

class SpigotLogger extends Logger {

  private val _logger = Bukkit.getLogger

  override def log(api: Boolean, logType: LogType, message: String): Unit = {
    val string = s"${color(logType)} ${if (api) apiPrefix else spigotPrefix} ${logType.getClass.getSimpleName} " +
      s"${ChatColor.WHITE}: $message"
    Bukkit.getConsoleSender.sendMessage(string)
  }

  private def level(logType: LogType): Level = logType match {
    case logger.Error() => Level.SEVERE
    case logger.Warning() => Level.WARNING
    case logger.Info() => Level.INFO
    case logger.IO() => Level.INFO
  }

  private def color(logType: LogType): ChatColor = logType match {
    case logger.Error() => ChatColor.RED
    case logger.Warning() => ChatColor.YELLOW
    case logger.Info() => ChatColor.GREEN
    case logger.IO() => ChatColor.BLUE
  }
}

object SpigotLogger {
  val apiPrefix = "[SkyBlock]"
  val spigotPrefix = "[SpigotSkyBlock]"
}
