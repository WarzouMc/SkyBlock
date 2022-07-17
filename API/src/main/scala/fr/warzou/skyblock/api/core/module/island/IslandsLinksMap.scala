package fr.warzou.skyblock.api.core.module.island

import java.util.UUID
import scala.collection.mutable

final class IslandsLinksMap {

  private val map = mutable.Map[UUID, String]()

  private[island] def put(uuid: UUID, fileName: String): Unit = {
    if (map.keysIterator.contains(uuid)) throw new IllegalArgumentException("Given uuid already exist in links map")
    if (map.valuesIterator.contains(fileName)) throw new IllegalArgumentException("Given file name already exist in links map")
    map.put(uuid, fileName)
  }

  def getFileName(uuid: UUID): Option[String] = map.get(uuid)

  def getUUID(fileName: String): Option[UUID] =
}
