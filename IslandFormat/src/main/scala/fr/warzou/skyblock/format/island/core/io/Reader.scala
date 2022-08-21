package fr.warzou.skyblock.format.island.core.io

import fr.warzou.skyblock.format.island.core.RawIsland

trait Reader {
  def read: RawIsland
}
