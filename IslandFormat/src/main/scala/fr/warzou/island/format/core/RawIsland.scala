package fr.warzou.island.format.core

import fr.warzou.island.format.core.common._
import fr.warzou.island.format.core.common.cuboid.Cuboid
import org.bukkit.block.Block

class RawIsland(val name: String, val minecraftVersion: Version, val cuboid: Cuboid, val blocks: List[Block]) {}
