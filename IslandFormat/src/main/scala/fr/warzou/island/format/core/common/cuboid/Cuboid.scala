package fr.warzou.island.format.core.common.cuboid

import org.bukkit.Location

class Cuboid(corner0: Location, corner1: Location) {

  def xSize(): Int = Math.abs(corner1.getBlockX - corner0.getBlockX)
  def ySize(): Int = Math.abs(corner1.getBlockY - corner0.getBlockY)
  def zSize(): Int = Math.abs(corner1.getBlockZ - corner0.getBlockZ)

}
