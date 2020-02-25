import java.awt.Graphics2D
import java.awt.BasicStroke
import java.awt.Color

class LaserBeam(d: Double, me: MovingEnemy, t: Tower) extends Entity {
    var a: CellPoint = me.pos
    var b: CellPoint = t.pos
	var bornTime: Double = d

    def tick(running_for: Double, delta: Double) = {
        if (running_for - bornTime >= 400)
            valid = false
    }

    def render(g: Graphics2D, running_for: Double, delta: Double) = {
        g.setColor(Color.RED)
        g.setStroke(new BasicStroke(10))
        val sa = a.toScreenPosition
        val sb = b.toScreenPosition
        g.drawLine(sa.x, sa.y, sb.x, sb.y)
    }
}