import engine.core.Renderer
import engine.helpers.{CellPoint, ScreenPoint}
import engine.loaders.SpriteLoader
import engine.Cst

import scala.math.{cos, sin, atan}
import java.awt.geom.AffineTransform
import scala.collection.mutable.{ArrayBuffer, Map, PriorityQueue}

/**
  * The MovingEnemy trait represents an enemy with a speed that can
  * move across the board.
  */
trait MovingEnemy extends Enemy {
	var speed: Double = 2.4
	var targetedCheckpoint: Int = -2
	var targetedCellPoint: CellPoint = new CellPoint(0, 0)
	var pos: CellPoint = new CellPoint(0, 0)

	def tick(time: Double, delta: Double) : Unit = {
		var cp = Game.map.checkpoints(targetedCheckpoint)
		val l = aStar(pos, targetedCellPoint)
		if (pos.distance(targetedCellPoint) <= speed * (delta / 1000)) {
			targetedCheckpoint = cp.next
			if (targetedCheckpoint == -1) {
				Game.health = (Game.health - 20) max 0
				valid = false
				return
			}
			cp = Game.map.checkpoints(targetedCheckpoint)
			val r = scala.util.Random
			targetedCellPoint = cp.a + new CellPoint(r.nextFloat, r.nextFloat) * (cp.b - cp.a)
		}

		val dist = pos.distance(targetedCellPoint)
		val dirx = (targetedCellPoint.x - pos.x) / dist
		val diry = (targetedCellPoint.y - pos.y) / dist
		pos += new CellPoint(dirx, diry) * speed * (delta / 1000)
	}

	def aStar(start: CellPoint, end: CellPoint): List[CellPoint] = {
		var queue = new PriorityQueue[(CellPoint, Double)]()( Ordering.by{ case (_, d) => d } ).reverse
		val parents = Map[CellPoint, CellPoint]()
		val scores = Map[CellPoint, Double](start -> 0).withDefaultValue(Double.PositiveInfinity)
		queue += ((start, start.distance(end)))

		while (!queue.isEmpty) {
			val (current, _) = queue.dequeue()

			// reached the end
			if (current.nearestMiddle() == end.nearestMiddle()) {
				val path = ArrayBuffer[CellPoint](end)
				var cursor = current
				while (cursor != start) {
					path += cursor
					cursor = parents(cursor)
				}
				return (path += start).reverse.toList
			}

			for (neighbor <- current.neighborCells.filter(c => Game.map.map(c.x.toInt)(c.y.toInt) == engine.map.Path)) {
				val score = scores(current) + current.distance(neighbor)
				if (score < scores(neighbor)) {
					parents(neighbor) = current
					scores(neighbor) = score
					queue = queue.filter( p => p._1 != neighbor )
					queue += ((neighbor, neighbor.distance(end)))
				}
			}
		}

		// should never arrive here
		return List()
	}
}