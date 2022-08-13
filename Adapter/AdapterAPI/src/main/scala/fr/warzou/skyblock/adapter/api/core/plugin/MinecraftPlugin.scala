package fr.warzou.skyblock.adapter.api.core.plugin

import fr.warzou.skyblock.adapter.api.common.logger.Logger
import fr.warzou.skyblock.utils.server.ServerAPI

import java.io.File

/**
 * This class allow to access at some server property.
 *
 * @version 0.0.1
 * @author Warzou
 */
trait MinecraftPlugin {

  val islandFolder: File = new File(dataFolder, "islands")

  /**
   * @return version with format major.minor.revision
   */
  def version: String

  /**
   * @return server api
   */
  def api: ServerAPI

  /**
   * @return plugin data folder
   */
  def dataFolder: File

  /**
   * @return a [[Logger]] implementation
   */
  def logger: Logger
}
