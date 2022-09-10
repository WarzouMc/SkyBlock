package fr.warzou.skyblock.nms.versioning.api

import fr.warzou.skyblock.adapter.api.AdapterAPI
import fr.warzou.skyblock.nms.versioning.api.core.block.BlockWrap
import fr.warzou.skyblock.nms.versioning.api.core.entity.EntityWrap
import fr.warzou.skyblock.nms.versioning.api.core.nbt.NBTTools
import fr.warzou.skyblock.nms.versioning.api.core.world.{NMSWorld, WorldType}
import fr.warzou.skyblock.utils.cuboid.Cuboid
import org.bukkit.World

trait NMSVersion {

  def getNBTTools: NBTTools

  def blockWrap[A](block: A): BlockWrap[A]

  def entityWrap[A](entity: A): EntityWrap[A]

  def getNMSWorld(world: World, string: WorldType): NMSWorld

  def enumerateEntities(adapter: AdapterAPI, cuboid: Cuboid): List[_]
}
