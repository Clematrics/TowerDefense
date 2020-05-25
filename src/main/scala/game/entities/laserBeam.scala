import engine.core.{Entity, Renderer}
import engine.helpers.CellPoint

import java.awt.Graphics2D
import java.awt.BasicStroke
import java.awt.Color

/**
  * The Short during Laser entity.
  *
  * @param d  Instant in the timeline when this entity is created
  * @param me The targeted enemy
  * @param t  Source tower
  * @param c  Color of the laser
  */
class LaserBeam(d: Double, me: MovingEnemy, t: Tower, c: Color) extends Entity {
	var a: CellPoint = me.pos
	var b: CellPoint = t.pos
	var bornTime: Double = d
	var clr: Color = c

	def tick(time: Double, delta: Double) = {
		if (bornTime + 400 < time)
			valid = false
	}

	def render(time: Double, delta: Double) = {
		Renderer.flyingEntities.setColor(clr)
		Renderer.flyingEntities.setStroke(new BasicStroke(6))
		val sa = a.toScreenPosition
		val sb = b.toScreenPosition
		Renderer.flyingEntities.drawLine(sa.x, sa.y, sb.x, sb.y)
	}
}