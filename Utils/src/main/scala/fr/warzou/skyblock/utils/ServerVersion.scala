package fr.warzou.skyblock.utils

import fr.warzou.skyblock.adapter.api.core.plugin.MinecraftPlugin

import java.util.regex.Pattern

case class ServerVersion(major: Int, minor: Int, revision: Int) {

  def >(version: ServerVersion): Boolean =
    major > version.major || (major == version.major && minor > version.minor) ||
      (major == version.major && minor == version.minor && revision > version.revision)

  def <(version: ServerVersion): Boolean =
    major < version.major || (major == version.major && minor < version.minor) ||
      (major == version.major && minor == version.minor && revision < version.revision)

  def >=(version: ServerVersion): Boolean = this > version || this == version

  def <=(version: ServerVersion): Boolean = this < version || this == version

  override def toString: String = s"$major.$minor.$revision"

  override def equals(obj: Any): Boolean = {
    if (obj == null)
      return false
    if (!obj.isInstanceOf[ServerVersion])
      return false

    val version = obj.asInstanceOf[ServerVersion]
    version.major == major && version.minor == minor && version.revision == revision
  }
}

object ServerVersion {

  val v1_12_2: ServerVersion = from("1.12.2")
  val v1_13_2: ServerVersion = from("1.13.2")
  val v1_14_4: ServerVersion = from("1.14.4")
  val v1_15_2: ServerVersion = from("1.15.2")
  val v1_16_5: ServerVersion = from("1.16.5")
  val v1_17_2: ServerVersion = from("1.17.2")
  val v1_18_2: ServerVersion = from("1.18.2")
  val v1_19_2: ServerVersion = from("1.19.2")
  val latest: ServerVersion = v1_19_2

  def from(plugin: MinecraftPlugin): ServerVersion = from(plugin.version)

  def from(string: String): ServerVersion = {
    val split = string.split('.')
    if (split.length != 3 && split.length != 2) throw new IllegalArgumentException(s"Any version is recognize in $string !")
    new ServerVersion(split(0).toInt, split(1).toInt, if (split.length == 2) 0 else split(2).toInt)
  }

  def fromRawString(string: String): ServerVersion = {
    val array = Pattern.compile("(\\d+(.\\d+){2})").split(string, 2)
    val _string = array.foldRight(string)((value: String, string: String) => string.replace(value, ""))
    val split = _string.split('.')
    if (split.length != 3 && split.length != 2) throw new IllegalArgumentException(s"Any version is recognize in $string !")
    from(s"${split(0).toInt}.${split(1).toInt}.${if (split.length == 2) 0 else split(2).toInt}")
  }
}