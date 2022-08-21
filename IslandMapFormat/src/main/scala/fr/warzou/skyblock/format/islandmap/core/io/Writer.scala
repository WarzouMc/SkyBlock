package fr.warzou.skyblock.format.islandmap.core.io

import fr.warzou.skyblock.format.islandmap.core.IslandMap

import java.io.OutputStream

trait Writer {

  def version: Int

  def write(outputStream: OutputStream, map: IslandMap): Unit
}
