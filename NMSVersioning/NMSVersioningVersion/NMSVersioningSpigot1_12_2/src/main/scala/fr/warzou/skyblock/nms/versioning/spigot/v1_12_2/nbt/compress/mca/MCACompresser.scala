package fr.warzou.skyblock.nms.versioning.spigot.v1_12_2.nbt.compress.mca

import fr.warzou.skyblock.nms.versioning.api.nbt.compress.mca
import net.minecraft.server.v1_12_R1.{ChunkRegionLoader, NBTCompressedStreamTools, WorldServer}

import java.io.{ByteArrayInputStream, ByteArrayOutputStream, DataOutput, DataOutputStream}

case class MCACompresser(defaultCompress: Array[Byte]) extends mca.MCACompresser {
  private val nbtTagCompound = {
    // bawi mais c'est pas du nbt ptn
    val inputStream = new ByteArrayInputStream(defaultCompress)
    NBTCompressedStreamTools.a(inputStream)
  }

  override def compress: Array[Byte] = {
    ???
  }

  override def notCompress: Array[Byte] = {
    val outputStream = new ByteArrayOutputStream()
    val dataOutput: DataOutput = new DataOutputStream(outputStream)
    NBTCompressedStreamTools.a(nbtTagCompound, dataOutput)
    outputStream.toByteArray
  }
}
