package fr.warzou.skyblock.nms.versioning.api.world

import java.io.File

trait NMSWorld {
  def worldType: WorldType

  def directory: File

  def level: File

  def regionFolder: File
}
