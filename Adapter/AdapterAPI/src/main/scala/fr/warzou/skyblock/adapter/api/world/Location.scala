package fr.warzou.skyblock.adapter.api.world

import fr.warzou.skyblock.adapter.api.AdapterAPI

trait Location {

  def getWorld: Option[String]

  def getBlockX: Int
  def getBlockY: Int
  def getBlockZ: Int

  def getX: Double
  def getY: Double
  def getZ: Double
}