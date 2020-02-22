import java.awt.image.BufferedImage
import java.awt.Point
import scala.io.Source

object MapLoader {
	def loadMap(str: String): Map = {
		val stream = getClass.getResourceAsStream(str + ".dat")
		val lines = Source.fromInputStream(stream).getLines

		/**
		  * Parsing map
		  *
		  * First line is the map name
		  * Second line contains the number N of checkpoints for monster
		  * N next lines are five integers, describing the coordinates of two points and the id of the following checkpoint
		  * The next line contains the number M of mobs to spawn during the wave
		  * M next lines are a double for the time of spawning, the checkpoint id for the location and finally the enemy's name
		  */
		val name = lines.next
		val N = lines.next.toInt
		var checkpoints: Array[MonsterCheckPoint] = Array()
		for(i <- 0 until N) {
			checkpoints +:= new MonsterCheckPoint(lines.next.split(" ").map((s: String) => { s.toInt }): _*)
		}
		val M = lines.next.toInt
		var wave: Array[Tuple3[Double, Int, String]] = Array()
		for (i <- 0 until M) {
			val strs = lines.next.split(" ")
			wave +:= (strs(0).toDouble, strs(1).toInt, strs(2))
		}

		val map = Array.ofDim[CellType](45, 30)
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

		return new Map(name, map, img, checkpoints, wave)
	}
}