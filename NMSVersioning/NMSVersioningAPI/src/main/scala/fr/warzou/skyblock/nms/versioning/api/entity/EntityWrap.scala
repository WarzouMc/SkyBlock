package fr.warzou.skyblock.nms.versioning.api.entity

case class EntityWrap[A](entity: A) {}
case object EntityWrap {
  def of[A](entity: A): EntityWrap[A] = EntityWrap(entity)
}
