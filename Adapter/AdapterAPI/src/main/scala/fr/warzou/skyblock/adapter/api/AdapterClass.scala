package fr.warzou.skyblock.adapter.api

import fr.warzou.skyblock.adapter.api.handler.AdapterHandler

abstract class AdapterClass(adapterHandler: AdapterHandler) {
  def adapter: AdapterAPI = new AdapterAPI(adapterHandler)
}
