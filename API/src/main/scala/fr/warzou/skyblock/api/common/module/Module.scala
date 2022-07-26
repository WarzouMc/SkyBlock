package fr.warzou.skyblock.api.common.module

trait Module {

  def onEnable(): Unit

  def onDisable(): Unit

}
