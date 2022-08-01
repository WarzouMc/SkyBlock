package fr.warzou.skyblock.api.core.player

import org.jetbrains.annotations.NotNull

import java.util.UUID

trait SkyBlockPlayer {

  def hasIsland: Boolean

  def island: Option[UUID]

  def setIsland(@NotNull uuid: UUID): Unit

  def removeIsland(@NotNull uuid: UUID): Unit
}
