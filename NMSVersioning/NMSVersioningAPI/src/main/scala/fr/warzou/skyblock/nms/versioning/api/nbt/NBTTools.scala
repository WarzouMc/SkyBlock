package fr.warzou.skyblock.nms.versioning.api.nbt

import fr.warzou.skyblock.nms.versioning.api.block.BlockWrap
import fr.warzou.skyblock.nms.versioning.api.entity.EntityWrap
import fr.warzou.skyblock.nms.versioning.api.nbt.compress.mca.MCACompresser

trait NBTTools {

  def parse[A](entity: EntityWrap[A]): Array[Byte]

  def parse[A](block: BlockWrap[A]): Array[Byte]

  def applyNBT[A](entity: EntityWrap[A], bytes: Array[Byte]): Unit

  def applyNBT[A](block: BlockWrap[A], bytes: Array[Byte]): Unit

  def mcaCompresser(array: Array[Byte]): MCACompresser
}
