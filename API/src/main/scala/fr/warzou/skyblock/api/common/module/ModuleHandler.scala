package fr.warzou.skyblock.api.common.module

import fr.warzou.skyblock.adapter.api.AdapterAPI
import fr.warzou.skyblock.api.core.island.Island
import fr.warzou.skyblock.utils.cuboid.Cuboid

import scala.collection.mutable.ListBuffer

abstract class ModuleHandler {

  protected var _modules: ListBuffer[Module] = ListBuffer.empty

  def adapter: AdapterAPI

  def createIsland(name: String, cuboid: Cuboid): Island

  def modules: List[Module] = _modules.toList

  def enableAllModules(): Unit

  def disableAllModules(): Unit

  protected def enableModule(module: Module): Unit = {
    _modules.addOne(module)
    module.enable()
  }

  protected def disableModule(module: Module): Unit = {
    _modules -= module
    module.disable()
  }

  def getModule[A <: Module](clazz: Class[A]): Option[A]
}
