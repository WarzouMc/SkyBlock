package fr.warzou.skyblock.adapter.api.entity

import fr.warzou.skyblock.adapter.api.AdapterAPI
import fr.warzou.skyblock.adapter.api.world.Location
import fr.warzou.skyblock.utils.cuboid.Cuboid

trait Entity {

  def location: Location

  def name: String

  def nbt: Array[Byte]
}

trait EntitiesGetter {
  def enumerateEntity(adapter: AdapterAPI, cuboid: Cuboid): List[Entity]
}
