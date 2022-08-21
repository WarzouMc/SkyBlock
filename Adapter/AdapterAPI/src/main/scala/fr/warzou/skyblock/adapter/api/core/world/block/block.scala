package fr.warzou.skyblock.adapter.api.core.world.block

import fr.warzou.skyblock.adapter.api.common.wrap.{Unwrapper, Wrappable, Wrapper}

/**
 * A wrap of minecraft block
 *
 * @version 0.0.1
 * @author Warzou
 */
trait Block extends Wrappable[Block] {

  /**
   * @return block type name
   */
  def name: String

  /**
   * @return block data
   */
  def data: Int

  /**
   * @return block is a tile entity
   */
  def isBlockEntity: Boolean

  /**
   * @return a NBTTagCompound under an optionally byte array shape representing this block
   */
  def nbt: Option[Array[Byte]]
}

/**
 * Wrapper for minecraft block to [[Block]] and [[Block]] to minecraft block.
 *
 * @tparam A minecraft block type
 * @version 0.0.1
 * @author Warzou
 */
abstract class BlockWrapper[A] extends Wrapper[A, Block] with Unwrapper[Block, A]