package fr.warzou.island.format.core

import fr.warzou.island.format.core.common._
import fr.warzou.island.format.core.common.block.FileBlock
import fr.warzou.island.format.core.common.cuboid.Cuboid
import fr.warzou.island.format.core.common.entity.FileEntity
import org.bukkit.block.Block
import org.bukkit.entity.Entity

class RawIsland(val name: String, val minecraftVersion: Version, val cuboid: Cuboid, val blocks: List[FileBlock], val entities: List[FileEntity]) {}
