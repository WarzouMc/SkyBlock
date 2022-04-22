package fr.warzou.island.format.reader
import fr.warzou.island.format.core.RawIsland
import org.jetbrains.annotations.NotNull

import java.io.{File, FileReader}

case object IslandFileReader {

  def read(@NotNull file: File): RawIsland = {
    val reader: FileReader = new FileReader(file)
    ???
  }

}
