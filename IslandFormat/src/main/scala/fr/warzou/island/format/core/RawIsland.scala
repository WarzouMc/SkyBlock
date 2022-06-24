package fr.warzou.island.format.core

import fr.warzou.island.format.core.common._
import fr.warzou.island.format.core.common.block.FileBlock
import fr.warzou.island.format.core.common.entity.FileEntity
import fr.warzou.skyblock.adapter.api.plugin.MinecraftPlugin
import fr.warzou.skyblock.utils.cuboid.Cuboid

class RawIsland(val plugin: MinecraftPlugin, val name: String, val minecraftVersion: Version, val cuboid: Cuboid, val blocks: List[FileBlock], val entities: List[FileEntity])
