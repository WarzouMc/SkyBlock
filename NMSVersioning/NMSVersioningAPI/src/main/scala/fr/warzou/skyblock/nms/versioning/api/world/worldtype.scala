package fr.warzou.skyblock.nms.versioning.api.world

sealed trait WorldType {
  def id: Int

  def name: String
}

case class Overworld() extends WorldType {
  override def id: Int = 0

  override def name: String = ""
}

case class Nether() extends WorldType {
  override def id: Int = -1

  override def name: String = "nether"
}

case class TheEnd() extends WorldType {
  override def id: Int = 1

  override def name: String = "the_end"
}

case class Custom(id: Int, name: String) extends WorldType