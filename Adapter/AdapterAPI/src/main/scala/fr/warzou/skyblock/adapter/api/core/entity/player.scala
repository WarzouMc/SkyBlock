package fr.warzou.skyblock.adapter.api.core.entity

import fr.warzou.skyblock.adapter.api.common.wrap.{Unwrapper, Wrapper}

import java.util.UUID

trait Player extends Entity {

  def uuid: UUID

  def isFlying: Boolean

  def dim: Int

  def foodLevel: Int

  def foodExhaustionLevel: Int

  def foodSaturationLevel: Int

  def foodTickTimer: Int

  def riddenEntity: Option[Entity]

  def shoulderEntityLeft: Option[Entity]

  def shoulderEntityRight: Option[Entity]

  def xpLevel: Int

  def xpPercent: Float

  def xpSeed: Int

  def xpTotal: Int

  def memberSince: Long

  def playTime: Long

  def permissionLevel: Int

}

abstract class PlayerWrapper[A]() extends Wrapper[A, Entity] with Unwrapper[Entity, A]