import engine.core.Renderer
import engine.helpers.{CellPoint, ScreenPoint}
import engine.loaders.SpriteLoader
import engine.Cst

import scala.math.{cos, sin, atan}
import scala.collection.mutable.{ArrayBuffer, Map, PriorityQueue}
import java.awt.geom.AffineTransform
import java.awt.Color

/**
  * The MovingEnemy trait represents an enemy with a speed that can
  * move across the board.
  */
trait MovingEnemy extends Enemy {
	var speed: Double = 2.4
	var targetedCheckpoint: Int = -2
	var targetedPos: CellPoint = new CellPoint(0, 0)
	var pos: CellPoint = new CellPoint(0, 0)

	// the path is the set of points the enemy needs to pass by.
	var path = ArrayBuffer[CellPoint]()
	var pathPlanned = false

	def tick(time: Double, delta: Double) : Unit = {
		if (!pathPlanned) {
			pathPlanned = true
			var tempPos = pos
			var tempPath = ArrayBuffer[CellPoint]()
			while (targetedCheckpoint != -1) {
				var cp = Game.map.checkpoints(targetedCheckpoint)
				val r = scala.util.Random
				var targetedPos = cp.a + new CellPoint(r.nextFloat, r.nextFloat) * (cp.b - cp.a)
				tempPath ++= aStar(tempPos, targetedPos).tail
				tempPos = targetedPos
				targetedCheckpoint = cp.next
			}
			path = smoothPath(pos +: tempPath).tail // the current pos is added for smoothing and then removed
		}

		if (path.isEmpty) {
			Game.health = (Game.health - 20) max 0
			valid = false
			return
		}

		val toTravel = speed * delta / 1000
		var distanceTraveled = 0.0
		while (distanceTraveled < toTravel && path.nonEmpty) {
			val d = pos.distance(path.head)
			if (toTravel < distanceTraveled + d) {
				val dirx = (path.head.x - pos.x) / d
				val diry = (path.head.y - pos.y) / d
				pos += new CellPoint(dirx, diry) * (toTravel - distanceTraveled)
				distanceTraveled = toTravel
			}
			else {
				pos = path.remove(0)
				distanceTraveled += d
			}
		}
	}

	def aStar(start: CellPoint, end: CellPoint): ArrayBuffer[CellPoint] = {
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
				return (path += start).reverse
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
		println("Warning, no path exists")
		return ArrayBuffer(start, end)
	}

	def smoothPath(p : ArrayBuffer[CellPoint]) : ArrayBuffer[CellPoint] = {
		var p1 = p.map(new CellPoint(_))
		var p2 = p.map(new CellPoint(_))

		for (step <- 1 to 10) {
			for (i <- 1 until p.size - 1) {
				val newpos = (p1(i - 1) + p1(i + 1)) / 2.0
				if (Game.map.map(newpos.x.toInt)(newpos.y.toInt) == engine.map.Path)
					p2(i) = newpos
			}
			val temp = p1
			p1 = p2
			p2 = temp
		}

		return p2
	}

	def render(time: Double, delta: Double): Unit = {
		if (Renderer.debugMode) {
			val xArr = path.map(_.toScreenPosition.x).toArray
			val yArr = path.map(_.toScreenPosition.y).toArray
			Renderer.debug.setColor(Color.RED)
			Renderer.debug.drawPolyline(xArr, yArr, path.size)
		}
	}
}