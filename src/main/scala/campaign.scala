import java.awt.image.BufferedImage
import java.awt.Color
import java.awt.Point
import scala.io.Source

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
class Map(str: String) {
	val name = str
	var map = Array.ofDim[CellType](45, 30)
	var checkpoints: Array[MonsterCheckPoint] = Array()

	val cfStream = getClass.getResourceAsStream(str + ".dat")
	val lines = Source.fromInputStream(cfStream).getLines
	for(line <- lines) {
		checkpoints +:= new MonsterCheckPoint(line.split(" ").map((s: String) => { s.toInt }): _*)
	}

	val img = SpriteLoader.fromResource(str + ".png")
	val bi = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB)
	val g = bi.createGraphics
	g.drawImage(img, 0, 0, null)
	g.dispose
	assert(img.getWidth(null) == 45 && img.getHeight(null) == 30)
	for (i <- 0 until 45)
		for (j <- 0 until 30)
			bi.getRGB(i, j) match {
				case 0xFF000000 => {
					map(i)(j) = TowerCell
				}
				case 0xFFFFFFFF => {
					map(i)(j) = Path
				}
			}

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
			List(new Map("map"))
		),
		new Campaign(
			"The sea of differential equations",
			List(new Map("map"))
		),
		new Campaign(
			"Number theory's battlefield",
			List(new Map("map"), new Map("map"))
		),
		new Campaign(
			"The shitfuckery of Logics and Semantics",
			List(new Map("map"), new Map("map"), new Map("map"))
		),
		new Campaign(
			"Into the maze of quantum theory",
			List(new Map("map"), new Map("map"), new Map("map"), new Map("map"))
		),
		new Campaign(
			"The curse of hypercomputation",
			List(new Map("map"), new Map("map"), new Map("map"), new Map("map"), new Map("map"))
		),
		new Campaign(
			"The story of Levi-Civita in Lorentzian manifolds",
			List(new Map("map"), new Map("map"), new Map("map"), new Map("map"), new Map("map"), new Map("map"))
		),
		new Campaign(
			"The castle of complexity classes",
			List(new Map("map"), new Map("map"), new Map("map"), new Map("map"), new Map("map"), new Map("map"), new Map("map"), new Map("map"), new Map("map"), new Map("map"), new Map("map"), new Map("map"), new Map("map"), new Map("map"))
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