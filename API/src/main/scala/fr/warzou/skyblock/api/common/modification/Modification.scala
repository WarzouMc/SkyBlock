package fr.warzou.skyblock.api.common.modification

trait Modification[A] {

  def modificationType: Type[A]

  def restore(): Unit

  /**
   * Save this modification in island file.
   *
   * Save a modification involve the modification is no more one.
   * To apply that logic a modification who will be save need to  will be remove in modifiable object or will not considered as more like a restorable and saveable modification.
   *
   * @return an optionally [[SavedModification]], None if save fail
   */
  def save(): SavedModification[A]
}
