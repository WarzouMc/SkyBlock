package fr.warzou.skyblock.adapter.spigot

import fr.warzou.skyblock.adapter.api.AdapterAPI
import fr.warzou.skyblock.adapter.spigot.handler.SpigotAdapterHandler
import org.bukkit.plugin.Plugin

class SpigotAdapter(plugin: Plugin) {

  private val adapterHandler = SpigotAdapterHandler(plugin)
  private val adapter = new AdapterAPI(adapterHandler)
}
