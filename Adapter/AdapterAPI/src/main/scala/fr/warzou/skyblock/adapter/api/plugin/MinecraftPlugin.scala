package fr.warzou.skyblock.adapter.api.plugin

import fr.warzou.skyblock.utils.server.ServerAPI

import java.io.File

trait MinecraftPlugin {

  /**
   * @return version with format major.minor.revision
   */
  def version: String

  def api: ServerAPI

  def getDataFolder: File

}
