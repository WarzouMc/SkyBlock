package fr.warzou.skyblock.adapter.api.core.world

import fr.warzou.skyblock.adapter.api.common.wrap.{Unwrapper, Wrappable, Wrapper}

trait Block extends Wrappable[Block] {

  def name: String

  def data: Int

  def isBlockEntity: Boolean

  def nbt: Option[Array[Byte]]
}

abstract class BlockWrapper[A] extends Wrapper[A, Block] with Unwrapper[Block, A] {

  def toCustom(block: A): Block = wrap(block)

  def fromCustom(block: Block): A = unwrap(block)

}