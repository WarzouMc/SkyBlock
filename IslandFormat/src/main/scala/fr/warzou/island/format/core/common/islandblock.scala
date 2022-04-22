package fr.warzou.island.format.core.common

class IslandBlock(location: IslandLocation, id: Int, data: Int, face: Face) {

}

sealed trait Face
case object Up extends Face
case object Down extends Face
case object North extends Face
case object South extends Face
case object West extends Face
case object Est extends Face
