import java.awt.Image

sealed trait CellType
case object Path extends CellType
case object TowerCell extends CellType

/**
  * A map in a game of TowerDefense.
  */
class Map(nameIn: String, mapIn: Array[Array[CellType]], mapImgIn: Image, checkpointsIn: Array[Checkpoint], waveIn: Array[Tuple3[Double, Int, String]]) {
	val name = nameIn
	var map = mapIn
	var mapImg = mapImgIn
	var checkpoints = checkpointsIn
	var wave = waveIn
}