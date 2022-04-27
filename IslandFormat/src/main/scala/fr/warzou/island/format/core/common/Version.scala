package fr.warzou.island.format.core.common

import org.bukkit.plugin.Plugin

import java.util.regex.Pattern

case class Version(major: Int, minor: Int, revision: Int) {
  override def toString: String = s"$major.$minor.$revision"
}

object Version {

  def from(plugin: Plugin): Version = from(plugin.getServer.getBukkitVersion)

  def from(string: String): Version = {
    val array = Pattern.compile("(\\d+(.\\d+){2})").split(string)
    val _string = array.foldRight(string)((value: String, string: String) => string.replace(value, ""))
    val split = _string.split('.')
    if (split.length != 3 && split.length != 2) throw new IllegalArgumentException(s"Any version is recognize in $string !")
    new Version(split(0).toInt, split(1).toInt, if (split.length == 2) 0 else split(2).toInt)
  }

}