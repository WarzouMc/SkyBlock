package fr.warzou.skyblock.nms.versioning.api

import fr.warzou.skyblock.adapter.api.AdapterAPI
import fr.warzou.skyblock.nms.versioning.api.block.BlockWrap
import fr.warzou.skyblock.nms.versioning.api.entity.EntityWrap
import fr.warzou.skyblock.nms.versioning.api.nbt.NBTTools
import fr.warzou.skyblock.utils.cuboid.Cuboid

trait NMSVersion {

  def getNBTTools: NBTTools

  def blockWrap[A](block: A): BlockWrap[A]

  def entityWrap[A](entity: A): EntityWrap[A]

  def enumerateEntities(adapter: AdapterAPI, cuboid: Cuboid): List[_]
}
