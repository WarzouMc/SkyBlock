package fr.warzou.skyblock.nms.versioning.api

import fr.warzou.skyblock.adapter.api.common.logger.Logger
import fr.warzou.skyblock.adapter.api.core.plugin.MinecraftPlugin
import fr.warzou.skyblock.utils.ServerVersion
import fr.warzou.skyblock.utils.server.{ServerAPI, Spigot}

import scala.collection.mutable

case object NMSVersioningAPI {

  private val loadedVersion = new mutable.HashMap[ServerVersion, NMSVersion]()

  def getVersionAPI(plugin: MinecraftPlugin): NMSVersion = getVersionAPI(Some(plugin.logger), plugin.api, ServerVersion.from(plugin))

  def getVersionAPI(api: ServerAPI, version: ServerVersion): NMSVersion = getVersionAPI(None, api, version)

  def getVersionAPI(logger: Option[Logger], api: ServerAPI, version: ServerVersion): NMSVersion =
    loadedVersion.getOrElse(version, createNMSVersion(logger, api, version))

  private def createNMSVersion(logger: Option[Logger], api: ServerAPI, version: ServerVersion): NMSVersion = {
    if (logger.isDefined) logger.get.log(api = false, s"NMS versioning load version $version.")
    else System.out.print(s"[${api.getClass.getSimpleName}SkyBlock] NMS versioning load version $version.")
    val nmsVersion = buildNMSVersion(api, version)
    loadedVersion.put(version, nmsVersion)
    nmsVersion
  }

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
