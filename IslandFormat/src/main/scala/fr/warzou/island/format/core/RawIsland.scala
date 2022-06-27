package fr.warzou.island.format.core

import fr.warzou.skyblock.adapter.api.entity.Entity
import fr.warzou.skyblock.adapter.api.plugin.MinecraftPlugin
import fr.warzou.skyblock.adapter.api.world.Block
import fr.warzou.skyblock.utils.Version
import fr.warzou.skyblock.utils.cuboid.Cuboid

class RawIsland(val plugin: MinecraftPlugin, val name: String, val minecraftVersion: Version, val cuboid: Cuboid,
                val blocks: List[Block], val entities: List[Entity])
