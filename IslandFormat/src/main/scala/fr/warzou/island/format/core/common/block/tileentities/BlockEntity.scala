package fr.warzou.island.format.core.common.block.tileentities

import org.bukkit.Material
import org.bukkit.block.Block

class BlockEntity {

}

case object BlockEntity {

  private val shulkerList: List[Material] = Material.values().filter(_.name().endsWith("SHULKER_BOX")).toList
  private val tileEntities: List[Material] = List(Material.SIGN, Material.CHEST, Material.TRAPPED_CHEST, Material.DISPENSER,
    Material.FURNACE, Material.BREWING_STAND, Material.HOPPER, Material.DROPPER, Material.BEACON, Material.NOTE_BLOCK,
    Material.PISTON_BASE, Material.PISTON_STICKY_BASE, Material.PISTON_EXTENSION, Material.PISTON_MOVING_PIECE,
    Material.JUKEBOX, Material.ENCHANTMENT_TABLE, Material.ENDER_CHEST, Material.ENDER_PORTAL, Material.SKULL,
    Material.COMMAND_CHAIN, Material.COMMAND_MINECART, Material.COMMAND_REPEATING, Material.END_GATEWAY, Material.STRUCTURE_BLOCK,
    Material.DAYLIGHT_DETECTOR, Material.DAYLIGHT_DETECTOR_INVERTED, Material.FLOWER_POT, Material.REDSTONE_COMPARATOR_OFF,
    Material.REDSTONE_COMPARATOR_ON, Material.BED_BLOCK, Material.CAULDRON) ::: shulkerList

  def isBlockEntity(block: Block): Boolean = isBlockEntity(block.getType)

  def isBlockEntity(material: Material): Boolean = material.isBlock && tileEntities.contains(material)

}