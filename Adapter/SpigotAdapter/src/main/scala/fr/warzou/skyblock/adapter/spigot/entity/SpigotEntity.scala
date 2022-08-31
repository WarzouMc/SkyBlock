package fr.warzou.skyblock.adapter.spigot.entity

import fr.warzou.skyblock.adapter.api.common.wrap.{Unwrapper, Wrapper}
import fr.warzou.skyblock.adapter.api.core.entity.{Entity, EntityWrapper}
import fr.warzou.skyblock.adapter.api.core.world.location.Location
import fr.warzou.skyblock.adapter.spigot.world.location.SpigotLocation
import fr.warzou.skyblock.nms.versioning.api.NMSVersioningAPI
import fr.warzou.skyblock.nms.versioning.api.core.entity.EntityWrap
import fr.warzou.skyblock.utils.ServerVersion
import fr.warzou.skyblock.utils.server.Spigot
import org.bukkit.{Bukkit, NamespacedKey, entity}

case class SpigotEntity(_entity: entity.Entity) extends Entity {
  override def location: Location = SpigotLocation.wrap(_entity.getLocation)

  override def name: String = NamespacedKey.minecraft(_entity.getType.name().toLowerCase).toString

  override def nbt: Array[Byte] =
    NMSVersioningAPI.getVersionAPI(Spigot(), ServerVersion.fromRawString(Bukkit.getBukkitVersion)).getNBTTools.parse(EntityWrap.of(_entity))

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