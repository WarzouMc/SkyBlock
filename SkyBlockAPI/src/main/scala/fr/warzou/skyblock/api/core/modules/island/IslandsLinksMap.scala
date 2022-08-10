package fr.warzou.skyblock.api.core.modules.island

import fr.warzou.skyblock.utils.collection.map.mutable.BijectiveMap

import java.util.UUID

private[island] class IslandsLinksMap {

  private[island] val map = BijectiveMap.createHashBijectiveMap[UUID, String]()

  private[island] def put(uuid: UUID, fileName: String): Unit = map.put(uuid, fileName)

  def getFileName(uuid: UUID): Option[String] = map.fromKey(uuid)

  def getUUID(fileName: String): Option[UUID] = map.fromValue(fileName)
}
