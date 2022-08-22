package fr.warzou.skyblock.nms.versioning.spigot.v1_14_4

import fr.warzou.skyblock.adapter.api.AdapterAPI
import fr.warzou.skyblock.nms.versioning.api
import fr.warzou.skyblock.nms.versioning.api.block.BlockWrap
import fr.warzou.skyblock.nms.versioning.api.entity.EntityWrap
import fr.warzou.skyblock.nms.versioning.api.nbt.NBTTools
import fr.warzou.skyblock.nms.versioning.api.world.NMSWorld
import fr.warzou.skyblock.nms.versioning.spigot.v1_14_4.world.NMSWorld
import fr.warzou.skyblock.utils.cuboid.Cuboid
import net.minecraft.server.v1_14_R1.AxisAlignedBB
import org.bukkit.{Bukkit, World}
import org.bukkit.block.Block
import org.bukkit.craftbukkit.v1_14_R1.CraftWorld
import org.bukkit.entity.{Entity, EntityType}

import scala.jdk.CollectionConverters.CollectionHasAsScala

class NMSVersion extends api.NMSVersion {
  private val nbtTools = new nbt.NBTTools

  override def getNBTTools: NBTTools = nbtTools

  override def blockWrap[A](_block: A): BlockWrap[A] = {
    if (!_block.isInstanceOf[Block]) throw new IllegalArgumentException("Cannot wrap a non-spigot block !")
    block.BlockWrap(_block.asInstanceOf[Block]).asInstanceOf[BlockWrap[A]]
  }

  override def entityWrap[A](_entity: A): EntityWrap[A] = {
    if (!_entity.isInstanceOf[Entity]) throw new IllegalArgumentException("Cannot wrap a non-spigot entity !")
    EntityWrap(_entity)
  }

  override def getNMSWorld(world: World): NMSWorld = NMSWorld(world)

  override def enumerateEntities(adapter: AdapterAPI, cuboid: Cuboid): List[_] = {
    val bb: AxisAlignedBB = new AxisAlignedBB(cuboid.minX, cuboid.minY, cuboid.minZ, cuboid.maxX, cuboid.maxY, cuboid.maxZ)
    val entityList: java.util.List[net.minecraft.server.v1_14_R1.Entity] = cuboid.minCorner(adapter).world.map(Bukkit.getWorld).getOrElse(Bukkit.getWorlds.get(0))
      .asInstanceOf[CraftWorld].getHandle.getEntities(null, bb, (_: net.minecraft.server.v1_14_R1.Entity) => true)
    entityList.asScala.map(_.getBukkitEntity).filterNot(_.getType == EntityType.PLAYER).toList
  }
}
