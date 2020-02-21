import java.awt.image.BufferedImage
import java.awt.Color
import java.awt.Point
import scala.io.Source
import java.awt.Image

sealed trait CellType
case object Path extends CellType
case object TowerCell extends CellType

class MonsterCheckPoint(seq: Int*) {
	val Seq(aX, aY, bX, bY, n) = seq
	val a: Point  = new Point(aX, aY)
	val b: Point  = new Point(bX, bY)
	val next: Int = n
}


/**
  * A map in a game of TowerDefense.
  */
class Map(nameIn: String, mapIn: Array[Array[CellType]], mapImgIn: Image, checkpointsIn: Array[MonsterCheckPoint], waveIn: Array[Tuple3[Double, Int, String]]) {
	val name = nameIn
	var map = mapIn
	var mapImg = mapImgIn
	var checkpoints = checkpointsIn
	var wave = waveIn
}

/**
  * 
  *
  * @param str
  * @param rnds
  */
class Campaign(str: String, rnds: List[Map]) {
	var name    = str
	var rounds  = rnds
}

/**
  * This class contains all available compaigns.
  */
object Campaigns {
	var selectedCampaign: Int = 0
	var campaigns = List(
		new Campaign(
			"The valley of prime numbers",
			List(MapLoader.loadMap("map"))
		),
		new Campaign(
			"The sea of differential equations",
			List(MapLoader.loadMap("map"))
		),
		new Campaign(
			"Number theory's battlefield",
			List(MapLoader.loadMap("map"), MapLoader.loadMap("map"))
		),
		new Campaign(
			"The shitfuckery of Logics and Semantics",
			List(MapLoader.loadMap("map"), MapLoader.loadMap("map"), MapLoader.loadMap("map"))
		),
		new Campaign(
			"Into the maze of quantum theory",
			List(MapLoader.loadMap("map"), MapLoader.loadMap("map"), MapLoader.loadMap("map"), MapLoader.loadMap("map"))
		),
		new Campaign(
			"The curse of hypercomputation",
			List(MapLoader.loadMap("map"), MapLoader.loadMap("map"), MapLoader.loadMap("map"), MapLoader.loadMap("map"), MapLoader.loadMap("map"))
		),
		new Campaign(
			"The story of Levi-Civita in Lorentzian manifolds",
			List(MapLoader.loadMap("map"), MapLoader.loadMap("map"), MapLoader.loadMap("map"), MapLoader.loadMap("map"), MapLoader.loadMap("map"), MapLoader.loadMap("map"))
		),
		new Campaign(
			"The castle of complexity classes",
			List(MapLoader.loadMap("map"), MapLoader.loadMap("map"), MapLoader.loadMap("map"), MapLoader.loadMap("map"), MapLoader.loadMap("map"), MapLoader.loadMap("map"), MapLoader.loadMap("map"), MapLoader.loadMap("map"), MapLoader.loadMap("map"), MapLoader.loadMap("map"), MapLoader.loadMap("map"), MapLoader.loadMap("map"), MapLoader.loadMap("map"), MapLoader.loadMap("map"))
		),
	)

	def previousCampaign(): Campaign = {
		selectedCampaign = (selectedCampaign - 1).max(0)
		return currentCampaign
	}
	def currentCampaign(): Campaign = {
		return campaigns(selectedCampaign)
	}
	def nextCampaign(): Campaign = {
		selectedCampaign = (selectedCampaign + 1).min(campaigns.length - 1)
		return currentCampaign
	}

}