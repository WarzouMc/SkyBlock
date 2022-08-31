package fr.warzou.skyblock.nms.versioning.api.core.block

import fr.warzou.skyblock.adapter.api.core.plugin.MinecraftPlugin
import fr.warzou.skyblock.nms.versioning.api.NMSVersioningAPI

trait BlockWrap[A] {

  def block: A

  def isBlockEntity: Boolean
}

case object BlockWrap {
  def of[A](plugin: MinecraftPlugin, block: A): BlockWrap[A] = NMSVersioningAPI.getVersionAPI(plugin).blockWrap(block)
}