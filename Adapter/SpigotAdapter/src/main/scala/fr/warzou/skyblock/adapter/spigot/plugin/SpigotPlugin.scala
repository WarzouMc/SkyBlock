package fr.warzou.skyblock.adapter.spigot.plugin

import fr.warzou.skyblock.adapter.api.core.plugin.MinecraftPlugin
import fr.warzou.skyblock.utils.server.Spigot
import fr.warzou.skyblock.utils.server.ServerAPI
import org.bukkit.plugin.Plugin

import java.io.File
import java.util.regex.Pattern

case class SpigotPlugin(plugin: Plugin) extends MinecraftPlugin {

  /**
   * @return version with format major.minor.revision
   */
  override def version: String = {
    val string = plugin.getServer.getBukkitVersion
    val array = Pattern.compile("(\\d+(.\\d+){2})").split(string)
    val _string = array.foldRight(string)((value: String, string: String) => string.replace(value, ""))
    val split = _string.split('.')
    if (split.length != 3 && split.length != 2) throw new IllegalArgumentException(s"Any version is recognize in $string !")
    s"${split(0).toInt}.${split(1).toInt}.${if (split.length == 2) 0 else split(2).toInt}"
  }

  override def api: ServerAPI = Spigot()

  override def dataFolder: File = plugin.getDataFolder
}
