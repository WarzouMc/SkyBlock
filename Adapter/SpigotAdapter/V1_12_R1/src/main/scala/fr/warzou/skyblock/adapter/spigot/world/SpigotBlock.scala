package fr.warzou.skyblock.adapter.spigot.world

import fr.il_totore.spigotmetadata.api.SpigotMetadataAPI
import fr.il_totore.spigotmetadata.api.nbt.{NBTManager, NBTOutputStream, NamedNBT}
import fr.warzou.skyblock.adapter.api.world.Block
import fr.warzou.skyblock.adapter.spigot.world.SpigotBlock.nbtManager
import net.minecraft.server.v1_12_R1.BlockPosition
import org.apache.commons.io.output.ByteArrayOutputStream
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld
import org.bukkit.{NamespacedKey, block}

import java.util.UUID

class SpigotBlock(_block: block.Block) extends Block {
  override def name: String = NamespacedKey.minecraft(_block.getType.name().toLowerCase()).toString

  override def data: Int = _block.getData

  override def isBlockEntity: Boolean = {
    val location = _block.getLocation
    val worldServer = location.getWorld.asInstanceOf[CraftWorld].getHandle
    worldServer.getTileEntity(new BlockPosition(location.getBlockX, location.getBlockY, location.getBlockZ)) != null
  }

  override def nbt: Option[Array[Byte]] = {
    if (!isBlockEntity) None
    else {
      val outputStream = new ByteArrayOutputStream()
      val nbtOutputStream = new NBTOutputStream(nbtManager, outputStream)
      nbtOutputStream.writeTag(nbtManager.getNBTTag(_block))
      Some(outputStream.toByteArray)
    }
  }

  override def equals(obj: Any): Boolean = {
    if (obj == null || !obj.isInstanceOf[Block])
      return false

    val block0 = obj.asInstanceOf[Block]
    if (block0.isBlockEntity) false
    else block0.name == name && block0.data == data
  }
}

private case object SpigotBlock {
  private val nbtManager = SpigotMetadataAPI.getAPI.getNBTManager
}