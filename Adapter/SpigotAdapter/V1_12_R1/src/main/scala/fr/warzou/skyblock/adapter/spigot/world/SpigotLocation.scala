package fr.warzou.skyblock.adapter.spigot.world

import fr.warzou.skyblock.adapter.api.world.{Block, Location}
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

  override def locationInWorld(name: String): Location = new SpigotLocation(Some(name), _x, _y, _z)

  override def block(world: String): Block =
    new SpigotBlock(Bukkit.getWorld(world).getBlockAt(blockX, blockY, blockZ))

  def toBukkit: bukkit.Location = new bukkit.Location(Bukkit.getWorld(_world.getOrElse(Bukkit.getWorlds.get(0).getName)), _x, _y, _z)
}

case object SpigotLocation {

  def toCustom(location: bukkit.Location): Location = SpigotLocation(Some(location.getWorld.getName), location.getX, location.getY, location.getZ)

  def toBukkit(location: Location): bukkit.Location = location.asInstanceOf[SpigotLocation].toBukkit
}
