package fr.warzou.skyblock.adapter.spigot

import fr.warzou.skyblock.adapter.api.AdapterClass
import fr.warzou.skyblock.adapter.spigot.handler.SpigotAdapterHandler
import org.bukkit.plugin.Plugin

private case class SpigotAdapter(plugin: Plugin) extends AdapterClass(SpigotAdapterHandler(plugin)) {}