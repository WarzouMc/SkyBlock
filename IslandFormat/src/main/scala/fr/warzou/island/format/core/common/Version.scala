package fr.warzou.island.format.core.common

case class Version(val major: Int, val minor: Int, val revision: Int) {

  override def toString: String = {
    s"$major.$minor.$revision"
  }

}
