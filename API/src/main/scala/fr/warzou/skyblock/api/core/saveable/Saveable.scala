package fr.warzou.skyblock.api.core.saveable

import java.io.File

trait Saveable {

  def file: File

  def save(): Boolean

}
