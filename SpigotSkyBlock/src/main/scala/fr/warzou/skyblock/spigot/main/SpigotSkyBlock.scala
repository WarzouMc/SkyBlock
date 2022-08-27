package fr.warzou.skyblock.spigot.main

import fr.warzou.skyblock.adapter.api.common.wrap.{Unwrapper, Wrapper}
import fr.warzou.skyblock.adapter.api.core.entity
import fr.warzou.skyblock.adapter.api.core.entity.Entity
import fr.warzou.skyblock.adapter.api.core.world.location.Location
import fr.warzou.skyblock.adapter.spigot.world.location.SpigotLocation
import fr.warzou.skyblock.adapter.spigot.world.world.SpigotWorld
import fr.warzou.skyblock.format.islandmap.core.IndividualWorld
import fr.warzou.skyblock.format.islandmap.io.v0.writer.IsmapWriterV0
import fr.warzou.skyblock.spigot.main.common.module.SpigotModuleHandler
import fr.warzou.skyblock.utils.{ServerVersion, TickUtils}
import fr.warzou.skyblock.utils.cuboid.Cuboid
import org.bukkit
import org.bukkit.World.Environment
import org.bukkit.{Bukkit, WorldCreator}
import org.bukkit.command.{Command, CommandSender}
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

import java.io.{File, FileOutputStream}
import java.util.UUID

class SpigotSkyBlock extends JavaPlugin {

  private val handler = SpigotModuleHandler(this)
  private val api = fr.warzou.skyblock.api.SkyBlock(handler)

  override def onEnable(): Unit = {
    getCommand("place").setExecutor(this)
    getCommand("tpp").setExecutor(this)
    api.enableAPI()

    val loc0 = api.adapter.createLocation(Bukkit.getWorlds.get(0).getName, 0, 100, 20)
    val loc1 = api.adapter.createLocation(Bukkit.getWorlds.get(0).getName, 10, 106, 30)
    val cuboid = Cuboid(loc0, loc1)
    val island = api.createIsland("An_island", cuboid)

    island.withFileName("faut_un_nom")
    island.save(true)

    //island.place(api.adapter.createLocation(20, 110, 20))

    worldTest()
  }

  private def worldTest(): Unit = {
    val worldCreator = new WorldCreator(getDataFolder.getPath + "/worlds/used/world_test")
    val world = worldCreator.createWorld()
    val spigotWorld = SpigotWorld(api.adapter.plugin, world)

    val individualWorld = IndividualWorld(ServerVersion.v1_12_2, UUID.randomUUID(), "test", api.islandList().head.uuid,
      TickUtils.millisToTick(System.currentTimeMillis()), None, None, spigotWorld, new entity.Player {
        override def uuid: UUID = UUID.randomUUID()

        override def isFlying: Boolean = false

        override def dim: Int = 0

        override def foodLevel: Int = 20

        override def foodExhaustionLevel: Int = 10

        override def foodSaturationLevel: Int = 20

        override def foodTickTimer: Int = 18

        override def riddenEntity: Option[Entity] = None

        override def shoulderEntityLeft: Option[Entity] = None

        override def shoulderEntityRight: Option[Entity] = None

        override def xpLevel: Int = 100

        override def xpPercent: Float = 0.682f

        override def xpSeed: Int = 23829

        override def xpTotal: Int = 202930

        override def memberSince: Long = TickUtils.millisToTick(System.currentTimeMillis())

        override def playTime: Long = 38392

        override def permissionLevel: Int = 0

        /**
         * @return entity location
         */
        override def location: Location = api.adapter.createLocation(920, 10, 38)

        /**
         * @return namespace ":" type
         */
        override def name: String = "minecraft:player"

        /**
         * @return NNBTTagCompound, under a byte array, shape who represent this entity
         */
        override def nbt: Array[Byte] = (28 :: 29 :: -62 :: 20 :: -71 :: 35 :: Nil).toArray.map(_.toByte)

        override def wrapper(): Wrapper[_, Entity] = throw new UnsupportedOperationException()

        override def unwrapper(): Unwrapper[Entity, _] = throw new UnsupportedOperationException()
      }:: Nil)

    val file = new File(getDataFolder, "test.ismap")
    file.createNewFile()
    val outputStream = new FileOutputStream(file)
    new IsmapWriterV0(api.adapter).write(outputStream, individualWorld)
  }

  override def onCommand(sender: CommandSender, command: Command, label: String, args: Array[String]): Boolean = {
    if (command.getName == "place") {
      val islandName = args(0)
      val location = SpigotLocation.wrap(sender.asInstanceOf[Player].getLocation()).appendXYZ(0, -1, 0)
      val island = api.getIsland(islandName)
      island.place(location)
      return true
    }

    if (command.getName == "tpp") {
      val player = sender.asInstanceOf[Player]
      val world = player.getWorld
      if (world.getName == "world") {
        player.teleport(new bukkit.Location(Bukkit.getWorld(getDataFolder.getPath + "/worlds/used/world_test"),
          player.getLocation.getX, player.getLocation.getY, player.getLocation.getZ))
      } else {
        player.teleport(new bukkit.Location(Bukkit.getWorld("world"), player.getLocation.getX,
          player.getLocation.getY, player.getLocation.getZ))
      }
      return true
    }
    false
  }
}
