package fr.warzou.skyblock.nms.versioning.spigot.v1_14_4.block

import fr.warzou.skyblock.nms.versioning.api.block
import net.minecraft.server.v1_13_R2.BlockPosition
import org.bukkit.block.Block
import org.bukkit.craftbukkit.v1_13_R2.CraftWorld

case class BlockWrap(_block: Block) extends block.BlockWrap[Block] {
  override def block: Block = _block

  override def isBlockEntity: Boolean = {
    val location = _block.getLocation
    val worldServer = location.getWorld.asInstanceOf[CraftWorld].getHandle
    worldServer.getTileEntity(new BlockPosition(location.getBlockX, location.getBlockY, location.getBlockZ)) != null
  }
}
