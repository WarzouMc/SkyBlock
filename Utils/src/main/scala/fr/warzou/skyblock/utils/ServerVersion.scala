package fr.warzou.skyblock.utils

import fr.warzou.skyblock.adapter.api.core.plugin.MinecraftPlugin

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

  val V1_12_1: ServerVersion = from("1.12.1")

  def from(plugin: MinecraftPlugin): ServerVersion = from(plugin.version)

  def from(string: String): ServerVersion = {
    val split = string.split('.')
    if (split.length != 3 && split.length != 2) throw new IllegalArgumentException(s"Any version is recognize in $string !")
    new ServerVersion(split(0).toInt, split(1).toInt, if (split.length == 2) 0 else split(2).toInt)
  }

}