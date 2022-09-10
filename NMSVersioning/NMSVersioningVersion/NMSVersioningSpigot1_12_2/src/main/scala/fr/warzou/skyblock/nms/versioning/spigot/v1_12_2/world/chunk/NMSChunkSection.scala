package fr.warzou.skyblock.nms.versioning.spigot.v1_12_2.world.chunk

import fr.warzou.skyblock.nms.versioning.api.core.world.chunk
import fr.warzou.skyblock.nms.versioning.api.utils.io.Nibble._
import fr.warzou.skyblock.nms.versioning.api.utils.io.NibbleArray
import net.minecraft.server.v1_12_R1.{Block, Chunk, ChunkSection, IBlockData}

import scala.collection.mutable.ListBuffer

case class NMSChunkSection(section: ChunkSection, source: Chunk) extends chunk.NMSChunkSection {
  override def y: Byte = section.getYPosition.toByte

  //todo check for same structure of chunkformat
  override def blocks: Array[Short] = iBlockDatas.map(data => Block.getId(data.getBlock).toShort)

  override def datas: NibbleArray = new NibbleArray(iBlockDatas.map(data => data.getBlock.toLegacyData(data).toNibble))

  override def blockLights: NibbleArray = new NibbleArray(section.getEmittedLightArray.asBytes().map(_.toNibble))

  override def skyLights: NibbleArray = new NibbleArray((section.getSkyLightArray.asBytes().map(_.toNibble)))

  override def toByteArray: Array[Byte] = {
    ListBuffer.empty[Byte]
      .addOne(y)
      .addAll(toByteArray(blocks))
      .addAll(NibbleArray.toByteArray(datas))
      .addAll(NibbleArray.toByteArray(blockLights))
      .addAll(NibbleArray.toByteArray(skyLights))
      .toArray
  }

  private val iBlockDatas: Array[IBlockData] = {
    val palette = section.getBlocks
    val blockDatas = new Array[IBlockData](16 * 16 * 16)
    for (i <- 0 until 16 * 16 * 16) {
      val x = i & 15
      val _y = i >> 8 & 15
      val z = i >> 4 & 15
      blockDatas(i) = palette.a(x, _y, z)
    }
    blockDatas
  }

  private def toByteArray(shortArray: Array[Short]): Array[Byte] = {
    val array = new Array[Byte](shortArray.length * 2)
    shortArray.indices.foreach(i => {
      array(i * 2) = ((shortArray(i) & 0xFF00) >> 8).toByte
      array(i * 2 + 1) = (shortArray(i) & 0x00FF).toByte
    })
    array
  }
}
