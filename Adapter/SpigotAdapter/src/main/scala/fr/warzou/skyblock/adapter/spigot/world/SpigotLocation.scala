package fr.warzou.skyblock.adapter.spigot.world

import fr.warzou.skyblock.adapter.api.world.Location
import org.bukkit
import org.bukkit.Bukkit

case class SpigotLocation(world: Option[String], x: Double, y: Double, z: Double) extends Location {

  override def getWorld: Option[String] = world

  override def getBlockX: Int = (x - x.floor).toInt

  override def getBlockY: Int = (y - y.floor).toInt

  override def getBlockZ: Int = (z - z.floor).toInt

  override def getX: Double = x

  override def getY: Double = y

  override def getZ: Double = z

  def toBukkit: bukkit.Location = new bukkit.Location(Bukkit.getWorld(world.getOrElse(Bukkit.getWorlds.get(0).getName)), x, y, z)
}

case object SpigotLocation {

  def toCustom(location: bukkit.Location): Location = SpigotLocation(Some(location.getWorld.getName), location.getX, location.getY, location.getZ)

  def toBukkit(location: Location): bukkit.Location = location.asInstanceOf[SpigotLocation].toBukkit
}
