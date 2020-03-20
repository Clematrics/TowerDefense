import engine.core.Renderer
import engine.helpers.{CellPoint, ScreenPoint}
import engine.loaders.SpriteLoader

import java.awt._
import scala.math.{cos, sin, atan}
import java.awt.geom.AffineTransform

/**
  * The MovingEnemy trait represents an enemy with a speed that can
  * move across the board.
  */
trait MovingEnemy extends Enemy {
	var speed: Double = 0.04
	var targetedCheckpoint: Int = -2
	var targetedCellPoint: CellPoint = new CellPoint(0, 0)
	var pos: CellPoint = new CellPoint(0, 0)

	def tick(time: Double, delta: Double) : Unit = {
		var cp = Game.map.checkpoints(targetedCheckpoint)
		if (pos.distance(targetedCellPoint) <= speed) {
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
		pos += new CellPoint(dirx, diry) * speed
	}
}