package fr.warzou.skyblock.utils.server

sealed trait ServerAPI
case class Spigot() extends ServerAPI
case class Sponge(modded: Boolean) extends ServerAPI