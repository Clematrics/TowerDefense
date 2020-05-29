import engine.core.Renderer
import engine.helpers.{CellPoint, ScreenPoint}
import engine.loaders.SpriteLoader
import engine.Cst

import scala.util.control.Breaks._
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

	// the path is the set of points describing the trajectory of the enemy.
	var path = ArrayBuffer[CellPoint]()
	var reachedGoal = false

	def tick(time: Double, delta: Double) : Unit = {
		if (path.isEmpty) {
			if (reachedGoal && pos.distance(targetedPos) < 0.1) { // no walls around blocking the path, so the enemy has reached the goal
				Game.health = (Game.health - damage) max 0
				valid = false
				return
			}
			else {
				val wallsAround = Game.entities.filter(_.isInstanceOf[WallTower]).map(_.asInstanceOf[WallTower]).filter(_.pos.distance(pos) <= 1.5)
				if (wallsAround.nonEmpty)
					wallsAround.head.damage(1)
				else
					computePath()
			}
		} else {
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
	}

	def aStar(start: CellPoint, end: CellPoint): (ArrayBuffer[CellPoint], Boolean) = {
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
				var isFullyFound = true
				while (cursor != start) {
					if (Game.map.map(cursor.x.toInt)(cursor.y.toInt) == engine.map.Wall) {
						path.clear()
						isFullyFound = false
					}
					else {
						path += cursor
					}
					cursor = parents(cursor)
				}
				return ((path += start).reverse, isFullyFound)
			}

			for (neighbor <- current.neighborCells.filter(c => Game.map.map(c.x.toInt)(c.y.toInt) == engine.map.Path || Game.map.map(c.x.toInt)(c.y.toInt) == engine.map.Wall)) {
				val score = scores(current) + current.distance(neighbor)
				if (score < scores(neighbor)) {
					parents(neighbor) = current
					scores(neighbor) = score
					queue = queue.filter( p => p._1 != neighbor )
					queue += ((neighbor, neighbor.distance(end)))
				}
			}
		}

		// no path found from start to end, the map layout is incorrect
		println("Warning, no path exists. Map layout incorrect")
		return (ArrayBuffer(start, end), false)
	}

	def smoothPath(p : ArrayBuffer[CellPoint]) : ArrayBuffer[CellPoint] = {
		var p1 = p.map(new CellPoint(_))
		var p2 = p.map(new CellPoint(_))

		for (step <- 1 to 10) {
			for (i <- 1 until p.size - 1) {
				val newpos = (p1(i - 1) + p1(i + 1)) / 2.0
				if (Game.map.map(newpos.x.toInt)(newpos.y.toInt) == engine.map.Path
				 || Game.map.map(newpos.x.toInt)(newpos.y.toInt) == engine.map.Wall)
					p2(i) = newpos
			}
			val temp = p1
			p1 = p2
			p2 = temp
		}

		return p2
	}

	def computePath() : Unit = {
		if (reachedGoal) {
			return
		}
		var tempPos = pos
		var tempPath = ArrayBuffer[CellPoint]()
		breakable ({ while (targetedCheckpoint != -1 || !reachedGoal) {
			var cp = Game.map.checkpoints(targetedCheckpoint)
			var tempTargetedPos = cp.randomPoint()
			val (pathFound, found) = aStar(tempPos, tempTargetedPos)
			tempPath ++= pathFound.tail
			if (found) {
				tempPos = targetedPos
				targetedCheckpoint = cp.next
				targetedPos = tempTargetedPos
				if (cp.next == -1)
					reachedGoal = true
			}
			else
				break()
		}})
		path = smoothPath(pos +: tempPath).tail // the current pos is added for smoothing and then removed
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