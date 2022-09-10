package fr.warzou.skyblock.adapter.spigot.world.world

import fr.warzou.skyblock.adapter.api.core.plugin.MinecraftPlugin
import fr.warzou.skyblock.adapter.api.core.world.world.{Chunk, Region}
import net.minecraft.server.v1_12_R1.ChunkRegionLoader

import java.io.File
import scala.collection.mutable.ListBuffer

case class SpigotRegion(plugin: MinecraftPlugin, dimension: SpigotDimension, file: File, x: Int, z: Int) extends Region {
  override val chunks: Array[Chunk] = {
    val chunks = new ListBuffer[Chunk]
    val world = dimension.world._world
    for (cx <- chunkX until chunkX + 32;
      cz <- chunkZ until chunkZ + 32) {
      if (dimension.nmsWorld.chunkExist(file, cx, cz))
        chunks.addOne(SpigotChunk(plugin, this, cx, cz))
    }
    chunks.toArray
  }
  println(s"chucks : ${chunks.length}")
}
