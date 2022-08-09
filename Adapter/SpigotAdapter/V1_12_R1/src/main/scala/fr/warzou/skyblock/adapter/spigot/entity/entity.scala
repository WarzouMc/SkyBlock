package fr.warzou.skyblock.adapter.spigot.entity

import fr.warzou.skyblock.adapter.api.AdapterAPI
import fr.warzou.skyblock.adapter.api.common.wrap.{Unwrapper, Wrapper}
import fr.warzou.skyblock.adapter.api.core.entity.{EntitiesGetter, Entity, EntityWrapper}
import fr.warzou.skyblock.adapter.api.core.world.Location
import fr.warzou.skyblock.adapter.spigot.world.SpigotLocation
import fr.warzou.skyblock.utils.cuboid.Cuboid
import net.minecraft.server.v1_12_R1.{AxisAlignedBB, NBTCompressedStreamTools, NBTTagCompound}
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftEntity
import org.bukkit.entity.EntityType
import org.bukkit.{Bukkit, NamespacedKey, entity}

import java.io.ByteArrayOutputStream
import scala.jdk.CollectionConverters.CollectionHasAsScala

case class SpigotEntity(_entity: entity.Entity) extends Entity {
  override def location: Location = SpigotLocation.wrap(_entity.getLocation)

  override def name: String = NamespacedKey.minecraft(_entity.getType.name().toLowerCase).toString

  override def nbt: Array[Byte] = {
    val nmsEntity = _entity.asInstanceOf[CraftEntity].getHandle
    val nbtTagCompound = new NBTTagCompound
    val byteArrayOutputStream = new ByteArrayOutputStream()

    if (_entity.getType == EntityType.PLAYER) {
      nmsEntity.save(nbtTagCompound)
    } else nmsEntity.c(nbtTagCompound)

    NBTCompressedStreamTools.a(nbtTagCompound, byteArrayOutputStream)
    byteArrayOutputStream.toByteArray
  }

  def withLocation(_location: Location): Entity = {
    val old = this
    new Entity {
      override def location: Location = _location

      override def name: String = old.name

      override def nbt: Array[Byte] = old.nbt

      override def wrapper(): Wrapper[_, Entity] = old.wrapper()

      override def unwrapper(): Unwrapper[Entity, _] = old.unwrapper()
    }
  }

  override def wrapper(): Wrapper[entity.Entity, Entity] = SpigotEntity

  override def unwrapper(): Unwrapper[Entity, entity.Entity] = SpigotEntity
}

case object SpigotEntity extends EntityWrapper[entity.Entity] {
  override def wrap(_entity: entity.Entity): Entity = new SpigotEntity(_entity)

  override def unwrap(wrappedEntity: Entity): entity.Entity = wrappedEntity match {
    case SpigotEntity(entity) => entity
    case _ =>
      val unknownResult = wrappedEntity.unwrapper().unwrap(wrappedEntity)
      unknownResult match {
        case entity: entity.Entity => entity
        case _ => throw new IllegalArgumentException(s"No Unwrapper found to parse ${wrappedEntity.getClass} into a ${classOf[entity.Entity]} !")
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