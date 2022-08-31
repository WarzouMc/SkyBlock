package fr.warzou.skyblock.nms.versioning.api.core.world

import fr.warzou.skyblock.nms.versioning.api.core.world.chunk.NMSChunk

import java.io.File

trait NMSWorld {
  def worldType: WorldType

  def directory: File

  def level: File

  def regionFolder: File

  def getChunk(x: Int, z: Int): NMSChunk

  def getChunk(x: Int, y: Int, z: Int): NMSChunk
}
