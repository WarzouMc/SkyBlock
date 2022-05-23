package fr.warzou.island.format.core.common.block.tileentities

import fr.warzou.island.format.core.common.block.NotLocateBlock
import net.minecraft.server.v1_12_R1.{BlockPosition, WorldServer}
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld
import org.bukkit.craftbukkit.v1_12_R1.block.CraftBlock

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

  def isBlockEntity(notLocateBlock: NotLocateBlock): Boolean = {
    val block = notLocateBlock.block
    val location = block.getLocation
    val worldServer = location.getWorld.asInstanceOf[CraftWorld].getHandle
    worldServer.getTileEntity(new BlockPosition(location.getBlockX, location.getBlockY, location.getBlockZ)) != null
  }

  def isBlockEntity(material: Material): Boolean = material.isBlock && tileEntities.contains(material)

}