package engine.map

import engine.helpers.CellPoint

/**
  * An anchor on the map to guide enemies across the land.
  * (Soon to be removed)
  */
class Checkpoint(seq: Int*) {
	private val Seq(aX, aY, bX, bY, n) = seq
	val a = new CellPoint(aX, aY)
	val b = new CellPoint(bX, bY)
	val next: Int = n

	def randomPoint() : CellPoint = {
		val r = scala.util.Random
		return a + (b - a) * r.nextDouble
	}
}