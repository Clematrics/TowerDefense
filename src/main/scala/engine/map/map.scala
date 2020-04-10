package engine.map

import java.awt.Image

sealed trait CellType
case object Path extends CellType
case object Wall extends CellType
case object EmptyTowerCell extends CellType
case object OccupiedTowerCell extends CellType

/**
  * A map in a game of TowerDefense.
  */
class Map(nameIn: String, exp: Int, mapIn: Array[Array[CellType]], mapLayoutIn: Image, mapImgIn: Image, checkpointsIn: Array[Checkpoint], waveIn: Array[(Double, Int, String)]) {
	val name = nameIn
	val expNeeded = exp
	var map = mapIn
	var mapLayout = mapLayoutIn
	var mapImg = mapImgIn
	var checkpoints = checkpointsIn
	var wave = waveIn
}