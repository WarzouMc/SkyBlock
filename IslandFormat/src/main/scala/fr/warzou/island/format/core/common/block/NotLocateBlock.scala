package fr.warzou.island.format.core.common.block

import org.bukkit.block._
import org.bukkit.inventory.ItemStack
import org.bukkit.metadata.MetadataValue
import org.bukkit.plugin.Plugin
import org.bukkit.{Bukkit, Chunk, Location, Material, World}

import java.util

class NotLocateBlock(val block: Block) extends Block {

  override def getData: Byte = block.getData

  override def getRelative(i: Int, i1: Int, i2: Int): Block = block.getRelative(i, i1, i2)

  override def getRelative(blockFace: BlockFace): Block = block.getRelative(blockFace)

  override def getRelative(blockFace: BlockFace, i: Int): Block = block.getRelative(blockFace, i)

  override def getType: Material = block.getType

  override def getTypeId: Int = block.getTypeId

  override def getLightLevel: Byte = block.getLightLevel

  override def getLightFromSky: Byte = block.getLightFromSky

  override def getLightFromBlocks: Byte = block.getLightFromBlocks

  override def getWorld: World = block.getWorld

  override def getX: Int = 0

  override def getY: Int = 0

  override def getZ: Int = 0

  override def getLocation: Location = new Location(Bukkit.getWorlds.get(0), 0, 0, 0)

  override def getLocation(location: Location): Location = block.getLocation(location)

  override def getChunk: Chunk = getLocation.getChunk

  override def setData(b: Byte): Unit = block.setData(b)

  override def setData(b: Byte, b1: Boolean): Unit = block.setData(b, b1)

  override def setType(material: Material): Unit = block.setType(material)

  override def setType(material: Material, b: Boolean): Unit = block.setType(material, b)

  override def setTypeId(i: Int): Boolean = block.setTypeId(i)

  override def setTypeId(i: Int, b: Boolean): Boolean = block.setTypeId(i, b)

  override def setTypeIdAndData(i: Int, b: Byte, b1: Boolean): Boolean = block.setTypeIdAndData(i, b, b1)

  override def getFace(block: Block): BlockFace = block.getFace(block)

  override def getState: BlockState = block.getState

  override def getBiome: Biome = getLocation.getBlock.getBiome

  override def setBiome(biome: Biome): Unit = block.setBiome(biome)

  override def isBlockPowered: Boolean = block.isBlockPowered

  override def isBlockIndirectlyPowered: Boolean = block.isBlockIndirectlyPowered

  override def isBlockFacePowered(blockFace: BlockFace): Boolean = block.isBlockFacePowered(blockFace)

  override def isBlockFaceIndirectlyPowered(blockFace: BlockFace): Boolean = block.isBlockFaceIndirectlyPowered(blockFace)

  override def getBlockPower(blockFace: BlockFace): Int = block.getBlockPower(blockFace)

  override def getBlockPower: Int = block.getBlockPower

  override def isEmpty: Boolean = block.isEmpty

  override def isLiquid: Boolean = block.isLiquid

  override def getTemperature: Double = block.getTemperature

  override def getHumidity: Double = block.getHumidity

  override def getPistonMoveReaction: PistonMoveReaction = block.getPistonMoveReaction

  override def breakNaturally(): Boolean = block.breakNaturally()

  override def breakNaturally(itemStack: ItemStack): Boolean = block.breakNaturally(itemStack)

  override def getDrops: util.Collection[ItemStack] = block.getDrops

  override def getDrops(itemStack: ItemStack): util.Collection[ItemStack] = block.getDrops(itemStack)

  override def setMetadata(s: String, metadataValue: MetadataValue): Unit = block.setMetadata(s, metadataValue)

  override def getMetadata(s: String): util.List[MetadataValue] = block.getMetadata(s)

  override def hasMetadata(s: String): Boolean = block.hasMetadata(s)

  override def removeMetadata(s: String, plugin: Plugin): Unit = block.removeMetadata(s, plugin)

  override def toString: String = block.toString

  override def equals(obj: Any): Boolean = {
    if (null == obj || !obj.isInstanceOf[Block])
      return false

    val other = obj.asInstanceOf[Block]
    (other.isEmpty == isEmpty && other.isBlockPowered == isBlockPowered && other.isLiquid == isLiquid && other.getType == getType
      && equalsState(other.getState))
  }

  private def equalsState(blockState: BlockState): Boolean = {
    blockState match {
      case sign: Sign => isCast[Sign](getState) && (cast[Sign](getState).getLines sameElements sign.getLines)
      case chest: Chest => equalsChest(chest)
      case furnace: Furnace => equalsFurnace(furnace)
      case brewingStand: BrewingStand => equalsBrewingStand(brewingStand)
      case hopper: Hopper => equalsHopper(hopper)
      case dropper: Dropper => equalsDropper(dropper)
      case dispenser: Dispenser => equalsDispenser(dispenser)
      case beacon: Beacon => equalsBeacon(beacon)
      case noteBlock: NoteBlock => isCast[NoteBlock](getState) && (cast[NoteBlock](getState).getNote == noteBlock.getNote)
      case jukebox: Jukebox => equalsJukebox(jukebox)
      case enchantingTable: EnchantingTable => isCast[EnchantingTable](getState) && (cast[EnchantingTable](getState).getCustomName == enchantingTable.getCustomName)
      case skull: Skull => equalsSkull(skull)
      case commandBlock: CommandBlock => equalsCommandBlock(commandBlock)
      case endGateway: EndGateway => equalsEndGateway(endGateway)
      case daylightDetector: DaylightDetector => true
      case structure: Structure => equalsStructure(structure)
      case flowerPot: FlowerPot => isCast[FlowerPot](getState) && (cast[FlowerPot](getState).getContents == flowerPot.getContents)
      case redstoneComparator: Comparator => true
      case bed: Bed => isCast[Bed](getState) && (cast[Bed](getState).getColor == bed.getColor)
      case shulkerBox: ShulkerBox => equalsShulkerBox(shulkerBox)
      case container: Container => isCast[Container](getState) && (cast[Container](getState).getInventory == container.getInventory)
      case _ => true
    }
  }

  private def equalsChest(chest: Chest): Boolean = {
    if (!isCast[Chest](getState))
      return false

    val thisChest = cast[Chest](getState)
    thisChest.getInventory == chest.getInventory && thisChest.getCustomName == chest.getCustomName
  }

  def equalsFurnace(furnace: Furnace): Boolean = {
    if (!isCast[Furnace](getState))
      return false

    val thisFurnace = cast[Furnace](getState)
    (thisFurnace.getInventory == furnace.getInventory && thisFurnace.getCustomName == furnace.getCustomName
      && thisFurnace.getBurnTime == furnace.getBurnTime && thisFurnace.getCookTime == furnace.getCookTime)
  }

  def equalsBrewingStand(brewingStand: BrewingStand): Boolean = {
    if (!isCast[BrewingStand](getState))
      return false

    val thisBrewingStand = cast[BrewingStand](getState)
    (thisBrewingStand.getSnapshotInventory == brewingStand.getSnapshotInventory && thisBrewingStand.getCustomName == brewingStand.getCustomName
      && thisBrewingStand.getBrewingTime == brewingStand.getBrewingTime && thisBrewingStand.getFuelLevel == brewingStand.getFuelLevel)
  }

  private def equalsHopper(hopper: Hopper): Boolean = {
    if (!isCast[Hopper](getState))
      return false

    val thisHopper = cast[Hopper](getState)
    thisHopper.getInventory == hopper.getInventory && thisHopper.getCustomName == hopper.getCustomName
  }

  private def equalsDropper(dropper: Dropper): Boolean = {
    if (!isCast[Dropper](getState))
      return false

    val thisDropper = cast[Dropper](getState)
    thisDropper.getInventory == dropper.getInventory && thisDropper.getCustomName == dropper.getCustomName
  }

  private def equalsDispenser(dispenser: Dispenser): Boolean = {
    if (!isCast[Dispenser](getState))
      return false

    val thisDispenser = cast[Dispenser](getState)
    thisDispenser.getInventory == dispenser.getInventory && thisDispenser.getCustomName == dispenser.getCustomName
  }

  def equalsBeacon(beacon: Beacon): Boolean = {
    if (!isCast[Beacon](getState))
      return false

    val thisBeacon = cast[Beacon](getState)
    (thisBeacon.getSnapshotInventory == beacon.getSnapshotInventory && thisBeacon.getCustomName == beacon.getCustomName
      && thisBeacon.getTier == beacon.getTier && thisBeacon.getPrimaryEffect == beacon.getPrimaryEffect
      && thisBeacon.getSecondaryEffect == beacon.getSecondaryEffect)
  }

  def equalsJukebox(jukebox: Jukebox): Boolean = {
    if (!isCast[Jukebox](getState))
      return false

    val thisJukebox = cast[Jukebox](getState)
    thisJukebox.getPlaying == jukebox.getPlaying && thisJukebox.isPlaying == jukebox.isPlaying
  }

  def equalsSkull(skull: Skull): Boolean = {
    if (!isCast[Skull](getState))
      return false

    val thisSkull = cast[Skull](getState)
    (thisSkull.hasOwner == skull.hasOwner && thisSkull.getOwningPlayer == skull.getOwningPlayer
      && thisSkull.getRotation == skull.getRotation)
  }

  def equalsCommandBlock(commandBlock: CommandBlock): Boolean = {
    if (!isCast[CommandBlock](getState))
      return false

    val thisCommandBlock = cast[CommandBlock](getState)
    thisCommandBlock.getCommand == commandBlock.getCommand && thisCommandBlock.getName == commandBlock.getName
  }

  def equalsEndGateway(endGateway: EndGateway): Boolean = {
    if (!isCast[EndGateway](getState))
      return false

    val thisEndGateway = cast[EndGateway](getState)
    thisEndGateway.getExitLocation == endGateway.getExitLocation && thisEndGateway.isExactTeleport == endGateway.isExactTeleport
  }

  private def equalsStructure(structure: Structure): Boolean = {
    if (!isCast[Structure](getState))
      return false

    val thisStructure = cast[Structure](getState)
    (thisStructure.getMetadata == structure.getMetadata && thisStructure.getRotation == structure.getRotation && thisStructure.getSeed == structure.getSeed
      && thisStructure.getAuthor == structure.getAuthor && thisStructure.isShowAir == structure.isShowAir && thisStructure.getStructureName == structure.getStructureName
      && thisStructure.getStructureSize == structure.getStructureSize && thisStructure.getMirror == structure.getMirror
      && thisStructure.getUsageMode == structure.getUsageMode && thisStructure.isIgnoreEntities == structure.isIgnoreEntities
      && thisStructure.isBoundingBoxVisible == structure.isBoundingBoxVisible && thisStructure.getIntegrity == structure.getIntegrity)
  }

  private def equalsShulkerBox(shulkerBox: ShulkerBox): Boolean = {
    if (!isCast[ShulkerBox](getState))
      return false

    val thisShulkerBox = cast[ShulkerBox](getState)
    (thisShulkerBox.getInventory == shulkerBox.getInventory && thisShulkerBox.getCustomName == shulkerBox.getCustomName
      && thisShulkerBox.getColor == shulkerBox.getColor)
  }

  private def isCast[A](obj: Object): Boolean = obj.isInstanceOf[A]

  private def cast[A](obj: Object): A = obj.asInstanceOf[A]
}
