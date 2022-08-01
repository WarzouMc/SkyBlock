package fr.warzou.skyblock.adapter.api

import fr.warzou.skyblock.adapter.api.common.handler.AdapterHandler

abstract case class AdapterClass(adapterHandler: AdapterHandler) {
  def adapter: AdapterAPI = new AdapterAPI(adapterHandler)
}
