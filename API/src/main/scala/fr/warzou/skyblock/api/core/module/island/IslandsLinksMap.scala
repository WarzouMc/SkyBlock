package fr.warzou.skyblock.api.core.module.island

import fr.warzou.skyblock.api.core.island.Island
import fr.warzou.skyblock.utils.ArrayUtils

import java.util.UUID
import scala.collection.mutable

final class IslandsLinksMap {

  private val map = mutable.Map[UUID, String]()

  def put(uuid: UUID, fileName: String): Unit = {
    if (ArrayUtils.contains(map.keysIterator, uuid)) throw new IllegalArgumentException("Given uuid already exist in links map")
    if (ArrayUtils.contains(map.valuesIterator, fileName)) throw new IllegalArgumentException("Given file name already exist in links map")
    map.put(uuid, fileName)
  }

  def getFileName(uuid: UUID): Option[String] = map.get(uuid)
}
