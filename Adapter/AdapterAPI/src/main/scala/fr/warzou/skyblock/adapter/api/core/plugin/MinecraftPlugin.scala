package fr.warzou.skyblock.adapter.api.core.plugin

import fr.warzou.skyblock.adapter.api.common.logger.Logger
import fr.warzou.skyblock.utils.server.ServerAPI

import java.io.File

trait MinecraftPlugin {

  val islandFolder: File = new File(dataFolder, "islands")

  /**
   * @return version with format major.minor.revision
   */
  def version: String

  def api: ServerAPI

  def dataFolder: File

  def logger: Logger
}
