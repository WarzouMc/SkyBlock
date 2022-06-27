package fr.warzou.skyblock.utils

import fr.warzou.skyblock.adapter.api.plugin.MinecraftPlugin

case class Version(major: Int, minor: Int, revision: Int) {

  def >(version: Version): Boolean =
    major > version.major || (major == version.major && minor > version.minor) ||
      (major == version.major && minor == version.minor && revision > version.revision)

  def <(version: Version): Boolean =
    major < version.major || (major == version.major && minor < version.minor) ||
      (major == version.major && minor == version.minor && revision < version.revision)

  def >=(version: Version): Boolean = this > version || this == version

  def <=(version: Version): Boolean = this < version || this == version

  override def toString: String = s"$major.$minor.$revision"

  override def equals(obj: Any): Boolean = {
    if (obj == null)
      return false
    if (!obj.isInstanceOf[Version])
      return false

    val version = obj.asInstanceOf[Version]
    version.major == major && version.minor == minor && version.revision == revision
  }
}

object Version {

  def from(plugin: MinecraftPlugin): Version = from(plugin.version)

  def from(string: String): Version = {
    val split = string.split('.')
    if (split.length != 3 && split.length != 2) throw new IllegalArgumentException(s"Any version is recognize in $string !")
    new Version(split(0).toInt, split(1).toInt, if (split.length == 2) 0 else split(2).toInt)
  }

}