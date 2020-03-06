import java.awt.Graphics2D
import java.awt.BasicStroke
import java.awt.Color

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
        Renderer.flyingEntities.setStroke(new BasicStroke(10))
        val sa = a.toScreenPosition
        val sb = b.toScreenPosition
		Renderer.flyingEntities.drawLine(sa.x, sa.y, sb.x, sb.y)
    }
}