package fr.warzou.skyblock.adapter.spigot.world.world

import fr.warzou.skyblock.adapter.api.core.plugin.MinecraftPlugin
import fr.warzou.skyblock.adapter.api.core.world.world.Chunk
import fr.warzou.skyblock.nms.versioning.api.NMSVersioningAPI

case class SpigotChunk(plugin: MinecraftPlugin, region: SpigotRegion, x: Int, z: Int) extends Chunk {
  private val nmsChunk = NMSVersioningAPI.getVersionAPI(plugin)
    .getNMSWorld(region.dimension.world._world, region.dimension.dimensionType).getChunk(x, z)

  override val toByteArray: Array[Byte] = nmsChunk.toCustomChunkFormat

}
