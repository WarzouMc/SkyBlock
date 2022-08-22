package fr.warzou.skyblock.utils.island

import fr.warzou.skyblock.adapter.api.core.plugin.MinecraftPlugin

import java.io.File

case object IslandMapUtils {

  def storedWorldDirectory(plugin: MinecraftPlugin): File = new File(plugin.dataFolder, s"worlds${File.pathSeparator}stored")

  def usedWorldDirectory(plugin: MinecraftPlugin): File = new File(plugin.dataFolder, s"worlds${File.pathSeparator}used")
}
