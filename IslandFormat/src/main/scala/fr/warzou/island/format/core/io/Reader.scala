package fr.warzou.island.format.core.io

import fr.warzou.island.format.core.RawIsland

trait Reader {
  def read: RawIsland
}
