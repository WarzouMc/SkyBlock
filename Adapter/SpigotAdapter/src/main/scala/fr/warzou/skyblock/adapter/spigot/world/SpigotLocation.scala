package fr.warzou.skyblock.adapter.spigot.world

import fr.warzou.skyblock.adapter.api.common.wrap.{Unwrapper, Wrapper}
import fr.warzou.skyblock.adapter.api.core.world.block.Block
import fr.warzou.skyblock.adapter.api.core.world.location.Location
import org.bukkit
import org.bukkit.Bukkit

case class SpigotLocation(_world: Option[String], _x: Double, _y: Double, _z: Double) extends Location {

  override def world: Option[String] = _world

  override def blockX: Int = _x.floor.toInt
  override def blockY: Int = _y.floor.toInt
  override def blockZ: Int = _z.floor.toInt

  override def x: Double = _x
  override def y: Double = _y
  override def z: Double = _z

  override def withWorld(name: String): Location = new SpigotLocation(Some(name), _x, _y, _z)

  override def block(world: String): Block =
    new SpigotBlock(Bukkit.getWorld(world).getBlockAt(blockX, blockY, blockZ))

  override def appendXYZ(x: Double, y: Double, z: Double): Location = SpigotLocation(_world, _x + x, _y + y, _z + z)

  def toBukkit: bukkit.Location = new bukkit.Location(Bukkit.getWorld(_world.getOrElse(Bukkit.getWorlds.get(0).getName)), _x, _y, _z)

  override def wrapper(): Wrapper[_, Location] = SpigotLocation

  override def unwrapper(): Unwrapper[Location, _] = SpigotLocation
}

case object SpigotLocation extends Wrapper[bukkit.Location, Location] with Unwrapper[Location, bukkit.Location] {

  override def wrap(bukkitLocation: bukkit.Location): SpigotLocation = SpigotLocation(Some(bukkitLocation.getWorld.getName), bukkitLocation.getX, bukkitLocation.getY, bukkitLocation.getZ)

  override def unwrap(wrappedLocation: Location): bukkit.Location = wrappedLocation match {
    case location: SpigotLocation => location.toBukkit
    case _ => new bukkit.Location(Bukkit.getWorld(wrappedLocation.world.getOrElse(Bukkit.getWorlds.get(0).getName)),
      wrappedLocation.x, wrappedLocation.y, wrappedLocation.z)
  }
}
