import org.bukkit.Bukkit
import org.bukkit.block.Block
import org.bukkit.plugin.java.JavaPlugin

object Test extends JavaPlugin {

  def test(): Unit = {
    Bukkit.getVersion
  }

}
