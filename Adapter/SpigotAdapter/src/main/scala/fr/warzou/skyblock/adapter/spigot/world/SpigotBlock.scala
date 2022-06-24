package fr.warzou.skyblock.adapter.spigot.world

import fr.warzou.skyblock.adapter.api.world.Block
import org.bukkit.{NamespacedKey, block}

case class SpigotBlock(block: block.Block) extends Block {
  override def name: String = NamespacedKey.minecraft(block.getType.name().toLowerCase()).toString

  override def data: Int = block.getData

  override def nbt: Array[Byte] = ???
}
