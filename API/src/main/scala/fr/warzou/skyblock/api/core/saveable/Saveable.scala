package fr.warzou.skyblock.api.core.saveable

import java.io.File

trait Saveable {

  def withFileName(name: String): Unit

  def fileName: String

  def file: File

  def save(): Boolean

}
