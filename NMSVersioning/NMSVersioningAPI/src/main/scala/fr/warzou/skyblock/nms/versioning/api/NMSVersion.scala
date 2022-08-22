package fr.warzou.skyblock.nms.versioning.api

import fr.warzou.skyblock.adapter.api.AdapterAPI
import fr.warzou.skyblock.nms.versioning.api.block.BlockWrap
import fr.warzou.skyblock.nms.versioning.api.entity.EntityWrap
import fr.warzou.skyblock.nms.versioning.api.nbt.NBTTools
import fr.warzou.skyblock.nms.versioning.api.world.NMSWorld
import fr.warzou.skyblock.utils.cuboid.Cuboid
import org.bukkit.World

trait NMSVersion {

  def getNBTTools: NBTTools

  def blockWrap[A](block: A): BlockWrap[A]

  def entityWrap[A](entity: A): EntityWrap[A]

  def getNMSWorld(world: World): NMSWorld

  def enumerateEntities(adapter: AdapterAPI, cuboid: Cuboid): List[_]
}
