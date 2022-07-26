package fr.warzou.skyblock.api.common.modification

trait SavedModification[A] extends Modification[A] {

  override def restore: Boolean = throw new UnsupportedOperationException()

  override def save: Option[SavedModification[A]] = throw new UnsupportedOperationException()

}
