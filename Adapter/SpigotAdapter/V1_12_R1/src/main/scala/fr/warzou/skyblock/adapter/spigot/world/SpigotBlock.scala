package fr.warzou.skyblock.adapter.spigot.world

import fr.warzou.skyblock.adapter.api.common.wrap.{Unwrapper, Wrapper}
import fr.warzou.skyblock.adapter.api.core.world.{Block, BlockWrapper}
import net.minecraft.server.v1_12_R1.{BlockPosition, NBTCompressedStreamTools, NBTTagCompound}
import org.apache.commons.io.output.ByteArrayOutputStream
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld
import org.bukkit.{NamespacedKey, block}

case class SpigotBlock(_block: block.Block) extends Block {
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
      val location = _block.getLocation
      val worldServer = location.getWorld.asInstanceOf[CraftWorld].getHandle
      val tileEntity = worldServer.getTileEntity(new BlockPosition(location.getBlockX, location.getBlockY, location.getBlockZ))
      val nbtTagCompound = new NBTTagCompound
      val outputStream = new ByteArrayOutputStream()

      tileEntity.save(nbtTagCompound)
      NBTCompressedStreamTools.a(nbtTagCompound, outputStream)
      Some(outputStream.toByteArray)
    }
  }

  override def equals(obj: Any): Boolean = {
    if (obj == null || !obj.isInstanceOf[Block]) return false

    val block0 = obj.asInstanceOf[Block]
    !block0.isBlockEntity && block0.name == name && block0.data == data
  }

  override def wrapper(): Wrapper[_, Block] = SpigotBlock

  override def unwrapper(): Unwrapper[Block, _] = SpigotBlock
}

case object SpigotBlock extends BlockWrapper[block.Block] {
  override def wrap(bukkitBlock: block.Block): Block = new SpigotBlock(bukkitBlock)

  override def unwrap(wrappedBlock: Block): block.Block = wrappedBlock match {
    case SpigotBlock(_block) => _block
    case _ =>
      val unknownResult = wrappedBlock.unwrapper().unwrap(wrappedBlock)
      unknownResult match {
        case block: block.Block => block
        case _ => throw new IllegalArgumentException(s"No Unwrapper found to parse ${wrappedBlock.getClass} into a ${classOf[block.Block]} !")
      }
  }
}