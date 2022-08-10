package fr.warzou.skyblock.nms.versioning.api

import fr.warzou.skyblock.adapter.api.core.plugin.MinecraftPlugin
import fr.warzou.skyblock.utils.ServerVersion
import fr.warzou.skyblock.utils.server.{ServerAPI, Spigot}

case object NMSVersioningAPI {

  def getVersionAPI(plugin: MinecraftPlugin): NMSVersion = getVersionAPI(plugin.api, ServerVersion.from(plugin))

  def getVersionAPI(api: ServerAPI, version: ServerVersion): NMSVersion = buildNMSVersion(api, version)

  private def buildNMSVersion(api: ServerAPI, version: ServerVersion): NMSVersion = {
    val classPath = buildClassPath(api, version)
    Class.forName(classPath).getConstructor().newInstance().asInstanceOf[NMSVersion]
  }

  private def buildClassPath(api: ServerAPI, version: ServerVersion): String =
    s"fr.warzou.skyblock.nms.versioning.${serverAPI(api)}.${serverVersion(version)}.NMSVersion"

  private def serverAPI(api: ServerAPI): String = api match {
    case Spigot() => "spigot"
    case _ => throw new IllegalArgumentException("Unknown server api !")
  }

  private def serverVersion(version: ServerVersion): String = s"v${version.major}_${version.minor}_${version.revision}"
}
