package fr.warzou.skyblock.api.core.module.island

import fr.warzou.skyblock.utils.collection.map.mutable.BijectiveMap

import java.util.UUID

final class IslandsLinksMap {

  private val map = BijectiveMap.createHashBijectiveMap[UUID, String]()

  private[island] def put(uuid: UUID, fileName: String): Unit = map.put(uuid, fileName)

  def getFileName(uuid: UUID): Option[String] = map.fromKey(uuid)

  def getUUID(fileName: String): Option[UUID] = map.fromValue(fileName)
}
