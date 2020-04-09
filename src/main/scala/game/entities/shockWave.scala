import java.awt.{BasicStroke, Color}

import engine.Cst
import engine.core.{Entity, Renderer}

/**
  * Represents a circular shock wave
  */
class ShockWave(var bornTime: Double, var t: RadiusTower, var c: Color) extends Entity {
	override def tick(time: Double, delta: Double): Unit = {
		if (bornTime + 500 < time)
			valid = false
	}

	override def render(time: Double, delta: Double): Unit = {
		Renderer.flyingEntities.setColor(c)
		Renderer.flyingEntities.setStroke(new BasicStroke(4))
		val sa = t.pos.toScreenPosition
		val side = (Cst.cellSize * (t.radius * (time-bornTime) / 500)).toInt
		Renderer.flyingEntities.drawOval(sa.x - side,sa.y - side, side * 2, side * 2)
	}
}
