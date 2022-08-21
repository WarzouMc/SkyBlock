package fr.warzou.skyblock.format.islandmap.core

import fr.warzou.skyblock.adapter.api.core.entity.Player
import fr.warzou.skyblock.adapter.api.core.world.location.Location
import fr.warzou.skyblock.adapter.api.core.world.sector.Sector
import fr.warzou.skyblock.adapter.api.core.world.world.World
import fr.warzou.skyblock.utils.ServerVersion

import java.util.UUID

sealed trait IslandMap {

  def uuid: UUID

  def name: String

  def startIsland: UUID

  def creation: Long

  def version: ServerVersion

  def spawnLocation: Option[Location]

  def memberSpawnLocation: Option[Location]

  def players: List[Player]
}

// map_type 1
case class SectorMap(version: ServerVersion, uuid: UUID, name: String, startIsland: UUID, creation: Long,
                     spawnLocation: Option[Location], memberSpawnLocation: Option[Location], sector: Sector, players: List[Player])
  extends IslandMap
// map_type 0
case class IndividualWorld(version: ServerVersion, uuid: UUID, name: String, startIsland: UUID, creation: Long,
                           spawnLocation: Option[Location], memberSpawnLocation: Option[Location], world: World,
                           players: List[Player])
  extends IslandMap