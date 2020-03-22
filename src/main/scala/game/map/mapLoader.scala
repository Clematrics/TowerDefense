import engine.loaders.SpriteLoader
import engine.map.{Checkpoint, Map, CellType, Path, EmptyTowerCell}
import engine.Cst

import java.awt.image.BufferedImage
import java.awt.Point
import scala.io.Source
import scala.collection.mutable._
import scala.math.Ordering.IntOrdering

/**
  * A map parser to load maps at runtime from specially formatted files.
  */
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
		val checkpoints: ArrayBuffer[Checkpoint] = ArrayBuffer()
		for(i <- 0 until N) {
			checkpoints += new Checkpoint(lines.next.split(" ").map((s: String) => { s.toInt }): _*)
		}

		val M = lines.next.toInt
		var wave: ArrayBuffer[Tuple3[Double, Int, String]] = ArrayBuffer()
		for (i <- 0 until M) {
			val strs = lines.next.split(" ")
			wave += ((strs(0).toDouble, strs(1).toInt, strs(2)))
		}
		wave = wave.sortBy(_._1)

		val map = Array.ofDim[CellType](Cst.mapWidth, Cst.mapHeight)
		val imgLayout = SpriteLoader.fromResource(str + "Layout.png")
		val bi = new BufferedImage(imgLayout.getWidth(null), imgLayout.getHeight(null), BufferedImage.TYPE_INT_ARGB)
		val g = bi.createGraphics
		g.drawImage(imgLayout, 0, 0, null)
		g.dispose
		assert(imgLayout.getWidth(null) == Cst.mapWidth && imgLayout.getHeight(null) == Cst.mapHeight)
		for (i <- 0 until Cst.mapWidth)
			for (j <- 0 until Cst.mapHeight)
				bi.getRGB(i, j) match {
					case 0xFF000000 => {
						map(i)(j) = EmptyTowerCell
					}
					case 0xFFFFFFFF => {
						map(i)(j) = Path
					}
				}

		val img = SpriteLoader.fromResource(str + "Background.png")

		return new Map(name, map, imgLayout, img, checkpoints.toArray, wave.toArray)
	}
}