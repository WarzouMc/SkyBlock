package fr.warzou.skyblock.api.core.saveable

import java.io.File

trait Saveable {

  def withFileName(name: String): Unit = withFileName(name, deleteOld = true)

  def withFileName(name: String, deleteOld: Boolean): Unit

  def fileName: String

  def file: File

  def save(force: Boolean): Boolean

}
