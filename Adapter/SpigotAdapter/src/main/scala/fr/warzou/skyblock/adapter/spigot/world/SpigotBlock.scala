package fr.warzou.skyblock.adapter.spigot.world

import fr.warzou.skyblock.adapter.api.common.wrap.{Unwrapper, Wrapper}
import fr.warzou.skyblock.adapter.api.core.world.block.{Block, BlockWrapper}
import fr.warzou.skyblock.nms.versioning.api.NMSVersioningAPI
import fr.warzou.skyblock.utils.ServerVersion
import fr.warzou.skyblock.utils.server.Spigot
import org.bukkit.{Bukkit, NamespacedKey, block}

case class SpigotBlock(_block: block.Block) extends Block {
  private val nmsVersioning = NMSVersioningAPI.getVersionAPI(Spigot(), ServerVersion.fromRawString(Bukkit.getBukkitVersion))

  override def name: String = NamespacedKey.minecraft(_block.getType.name().toLowerCase()).toString

  override def data: Int = _block.getData

  override def isBlockEntity: Boolean = nmsVersioning.blockWrap(_block).isBlockEntity

  override def nbt: Option[Array[Byte]] = {
    if (!isBlockEntity) None
    else Some(nmsVersioning.getNBTTools.parse(nmsVersioning.blockWrap(_block)))
  }

  override def equals(obj: Any): Boolean = {
    if (obj == null || !obj.isInstanceOf[Block]) return false

    val block0 = obj.asInstanceOf[Block]
    !block0.isBlockEntity && block0.name == name && block0.data == data
  }

  override def wrapper(): Wrapper[_, Block] = SpigotBlock

  override def unwrapper(): Unwrapper[Block, _] = SpigotBlock
}

case object SpigotBlock extends BlockWrapper[block.Block] {
  override def wrap(bukkitBlock: block.Block): Block = new SpigotBlock(bukkitBlock)

  override def unwrap(wrappedBlock: Block): block.Block = wrappedBlock match {
    case SpigotBlock(_block) => _block
    case _ =>
      val unknownResult = wrappedBlock.unwrapper().unwrap(wrappedBlock)
      unknownResult match {
        case block: block.Block => block
        case _ => throw new IllegalArgumentException(s"No Unwrapper found to parse ${wrappedBlock.getClass} into a ${classOf[block.Block]} !")
      }
  }
}