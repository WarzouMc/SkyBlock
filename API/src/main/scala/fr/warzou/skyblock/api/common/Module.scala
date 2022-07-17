package fr.warzou.skyblock.api.common

trait Module {

  def onEnable(): Unit

  def onDisable(): Unit

}
