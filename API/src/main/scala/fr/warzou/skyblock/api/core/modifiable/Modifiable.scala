package fr.warzou.skyblock.api.core.modifiable

import fr.warzou.skyblock.api.common.modification.{Modification, Type}
import fr.warzou.skyblock.api.core.saveable.Saveable
import org.jetbrains.annotations.Nullable

trait Modifiable extends Saveable {

  def modify[A](@Nullable oldValue: A, @Nullable newValue: A, modificationType: Type[A]): Modification[A]

  def modifications: List[Modification[_]]

  def restoreAll(): Boolean
}
