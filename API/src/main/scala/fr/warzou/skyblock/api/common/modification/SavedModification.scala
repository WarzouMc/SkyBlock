package fr.warzou.skyblock.api.common.modification

final case class SavedModification[A](private val _type: Type[A]) extends Modification[A] {

  private val when = System.currentTimeMillis()

  def whenSaved: Long = when

  override def restore(): Unit = throw new UnsupportedOperationException()

  override def save(): SavedModification[A] = throw new UnsupportedOperationException()

  override def modificationType: Type[A] = _type
}

case object SavedModification {
  def fromModification[A](modification: Modification[A]): SavedModification[A] = SavedModification(modification.modificationType)
}
