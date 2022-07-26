package fr.warzou.skyblock.adapter.spigot.entity

import fr.il_totore.spigotmetadata.api.SpigotMetadataAPI
import fr.il_totore.spigotmetadata.api.nbt.NBTOutputStream
import fr.warzou.skyblock.adapter.api.AdapterAPI
import fr.warzou.skyblock.adapter.api.common.wrap.{Unwrapper, Wrappable, Wrapper}
import fr.warzou.skyblock.adapter.api.entity.{EntitiesGetter, Entity, EntityWrapper}
import fr.warzou.skyblock.adapter.api.world.Location
import fr.warzou.skyblock.adapter.spigot.world.SpigotLocation
import fr.warzou.skyblock.utils.cuboid.Cuboid
import net.minecraft.server.v1_12_R1.AxisAlignedBB
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld
import org.bukkit.{Bukkit, NamespacedKey, entity}

import java.io.ByteArrayOutputStream
import scala.jdk.CollectionConverters.CollectionHasAsScala

case class SpigotEntity(_entity: entity.Entity) extends Entity {
  override def location: Location = SpigotLocation.toCustom(_entity.getLocation)

  override def name: String = NamespacedKey.minecraft(_entity.getType.name().toLowerCase).toString

  override def nbt: Array[Byte] = {
    val byteArrayOutputStream = new ByteArrayOutputStream()
    val nbtOutputStream = new NBTOutputStream(SpigotEntity.nbtManager, byteArrayOutputStream)
    nbtOutputStream.writeTag(SpigotEntity.nbtManager.getNBTTag(_entity))
    byteArrayOutputStream.toByteArray
  }

  override def wrapper: Wrapper[entity.Entity, Entity] = SpigotEntity

  override def unwrapper: Unwrapper[Entity, entity.Entity] = SpigotEntity
}

case object SpigotEntity extends EntityWrapper[entity.Entity] {
  private val nbtManager = SpigotMetadataAPI.getAPI.getNBTManager

  override def wrap(_entity: entity.Entity): Entity = new SpigotEntity(_entity)

  override def unwrap(wrappedEntity: Entity): entity.Entity = wrappedEntity match {
    case SpigotEntity(entity) => entity
    case _ => {
      val unknownResult = wrappedEntity.unwrapper.unwrap(wrappedEntity)
      unknownResult match {
        case entity: entity.Entity => entity
        case _ => throw new IllegalArgumentException(s"No Unwrapper found to parse ${wrappedEntity.getClass} into a ${classOf[entity.Entity]} !")
      }
    }
  }
}


case object SpigotEntitiesGetter extends EntitiesGetter {
  override def enumerateEntity(adapter: AdapterAPI, cuboid: Cuboid): List[Entity] = {
    val bb: AxisAlignedBB = new AxisAlignedBB(cuboid.minX, cuboid.minY, cuboid.minZ, cuboid.maxX, cuboid.maxY, cuboid.maxZ)
    val entityList: java.util.List[net.minecraft.server.v1_12_R1.Entity] = cuboid.minCorner(adapter).world.map(Bukkit.getWorld).getOrElse(Bukkit.getWorlds.get(0))
      .asInstanceOf[CraftWorld].getHandle.getEntities(null, bb, (_: net.minecraft.server.v1_12_R1.Entity) => true)
    entityList.asScala.map(_.getBukkitEntity).map(SpigotEntity.toCustom(_)).toList
  }
}