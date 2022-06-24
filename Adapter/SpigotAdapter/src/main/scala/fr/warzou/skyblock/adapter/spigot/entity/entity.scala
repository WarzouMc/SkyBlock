package fr.warzou.skyblock.adapter.spigot.entity

import fr.il_totore.spigotmetadata.api.SpigotMetadataAPI
import fr.il_totore.spigotmetadata.api.nbt.NBTOutputStream
import fr.warzou.skyblock.adapter.api.AdapterAPI
import fr.warzou.skyblock.adapter.api.entity.{Entity, EntitiesGetter}
import fr.warzou.skyblock.adapter.api.world.Location
import fr.warzou.skyblock.adapter.spigot.entity.SpigotEntity.SpigotGetter
import fr.warzou.skyblock.adapter.spigot.world.SpigotLocation
import fr.warzou.skyblock.utils.cuboid.Cuboid
import net.minecraft.server.v1_12_R1.AxisAlignedBB
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld
import org.bukkit.{NamespacedKey, entity}

import java.io.ByteArrayOutputStream
import scala.jdk.CollectionConverters.CollectionHasAsScala

case class SpigotEntity(entity: entity.Entity) extends Entity {
  override def location: Location = SpigotLocation.toCustom(entity.getLocation)

  override def name: String = NamespacedKey.minecraft(entity.getType.name().toLowerCase).toString

  override def nbt: Array[Byte] = {
    val byteArrayOutputStream = new ByteArrayOutputStream()
    val nbtOutputStream = new NBTOutputStream(SpigotEntity.nbtManager, byteArrayOutputStream)
    nbtOutputStream.writeTag(SpigotEntity.nbtManager.getNBTTag(entity))
    byteArrayOutputStream.toByteArray
  }
}

case object SpigotEntity {
  private val nbtManager = SpigotMetadataAPI.getAPI.getNBTManager

  def toCustom(entity: entity.Entity): Entity = new SpigotEntity(entity)
}


case object SpigotEntitiesGetter extends EntitiesGetter {
  override def enumerateEntity(adapter: AdapterAPI, cuboid: Cuboid): List[Entity] = {
    val bb: AxisAlignedBB = new AxisAlignedBB(cuboid.minX, cuboid.minY, cuboid.minZ, cuboid.maxX, cuboid.maxY, cuboid.maxZ)
    val entityList: java.util.List[net.minecraft.server.v1_12_R1.Entity] = cuboid.minCorner(adapter).getWorld.asInstanceOf[CraftWorld]
      .getHandle.getEntities(null, bb, (e: net.minecraft.server.v1_12_R1.Entity) => true)
    entityList.asScala.map(_.getBukkitEntity).map(SpigotEntity.toCustom(_)).toList
  }
}