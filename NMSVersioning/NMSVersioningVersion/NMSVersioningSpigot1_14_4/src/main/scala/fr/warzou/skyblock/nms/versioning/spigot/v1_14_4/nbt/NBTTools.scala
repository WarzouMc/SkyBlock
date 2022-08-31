package fr.warzou.skyblock.nms.versioning.spigot.v1_14_4.nbt

import fr.warzou.skyblock.nms.versioning.api.core.block.BlockWrap
import fr.warzou.skyblock.nms.versioning.api.core.entity.EntityWrap
import fr.warzou.skyblock.nms.versioning.api.core.nbt
import net.minecraft.server.v1_14_R1.{BlockPosition, NBTCompressedStreamTools, NBTTagCompound, NBTTagInt}
import org.bukkit.block.Block
import org.bukkit.craftbukkit.v1_14_R1.CraftWorld
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftEntity
import org.bukkit.entity.{Entity, EntityType}

import java.io.{ByteArrayInputStream, ByteArrayOutputStream}

class NBTTools extends nbt.NBTTools {
  override def parse[A](entityWrap: EntityWrap[A]): Array[Byte] = {
    if (!entityWrap.entity.isInstanceOf[Entity]) throw new IllegalArgumentException("Not spigot entity !")
    val entity = entityWrap.entity.asInstanceOf[Entity]
    val nmsEntity = entity.asInstanceOf[CraftEntity].getHandle
    val nbtTagCompound = new NBTTagCompound

    if (entity.getType == EntityType.PLAYER) nmsEntity.save(nbtTagCompound)
    else nmsEntity.c(nbtTagCompound)

    parse(nbtTagCompound)
  }

  override def parse[A](blockWrap: BlockWrap[A]): Array[Byte] = {
    if (!blockWrap.block.isInstanceOf[Block]) throw new IllegalArgumentException("Not spigot block !")
    val block = blockWrap.block.asInstanceOf[Block]
    val location = block.getLocation
    val worldServer = location.getWorld.asInstanceOf[CraftWorld].getHandle
    val tileEntity = worldServer.getTileEntity(new BlockPosition(location.getBlockX, location.getBlockY, location.getBlockZ))
    val nbtTagCompound = new NBTTagCompound

    tileEntity.save(nbtTagCompound)
    parse(nbtTagCompound)
  }

  override def applyNBT[A](entityWrap: EntityWrap[A], bytes: Array[Byte]): Unit = {
    if (!entityWrap.entity.isInstanceOf[Entity]) throw new IllegalArgumentException("Not spigot entity !")
    val entity = entityWrap.entity.asInstanceOf[Entity]
    val nbtTagCompound = toNBTTagCompound(bytes)
    val craftEntity = entity.asInstanceOf[CraftEntity]

    craftEntity.getHandle.f(nbtTagCompound)
  }

  override def applyNBT[A](blockWrap: BlockWrap[A], bytes: Array[Byte]): Unit = {
    if (!blockWrap.block.isInstanceOf[Block]) throw new IllegalArgumentException("Not spigot block !")
    val block = blockWrap.block.asInstanceOf[Block]
    val nbtTagCompound = toNBTTagCompound(bytes)
    val blockLocation = block.getLocation
    val worldServer = blockLocation.getWorld.asInstanceOf[CraftWorld].getHandle
    val tileEntity = worldServer.getTileEntity(new BlockPosition(blockLocation.getBlockX, blockLocation.getBlockY, blockLocation.getBlockZ))

    nbtTagCompound.set("x", new NBTTagInt(blockLocation.getBlockX))
    nbtTagCompound.set("y", new NBTTagInt(blockLocation.getBlockY))
    nbtTagCompound.set("z", new NBTTagInt(blockLocation.getBlockZ))
    tileEntity.load(nbtTagCompound)
  }

  private def parse(nbtTagCompound: NBTTagCompound): Array[Byte] = {
    val outputStream = new ByteArrayOutputStream()
    NBTCompressedStreamTools.a(nbtTagCompound, outputStream)
    outputStream.toByteArray
  }

  private def toNBTTagCompound(bytes: Array[Byte]): NBTTagCompound = NBTCompressedStreamTools.a(new ByteArrayInputStream(bytes))
}
