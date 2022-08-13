package fr.warzou.skyblock.utils.server

/**
 * A server api (Sponge, Spigot, ...)
 *
 * @version 0.0.1
 * @author Warzou
 */
sealed trait ServerAPI
case class Spigot() extends ServerAPI
case class Sponge(modded: Boolean) extends ServerAPI