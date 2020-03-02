import java.awt.Graphics2D
import java.awt.BasicStroke
import java.awt.Color

class StableLaserBeam(d: Double, me: MovingEnemy, t: Tower, c: Color) extends Entity {
    var a: CellPoint = me.pos
    var b: CellPoint = t.pos
	var bornTime: Double = d
	var clr: Color = c
	
    def tick(running_for: Double, delta: Double) = {
        
    }

    override def render(g: Graphics2D) = {
        g.setColor(clr)
        g.setStroke(new BasicStroke(10))
        val sa = a.toScreenPosition
        val sb = b.toScreenPosition
		g.drawLine(sa.x, sa.y, sb.x, sb.y)
    }
}

class LaserBeam(d: Double, me: MovingEnemy, t: Tower, c: Color) extends StableLaserBeam(d, me, t, c) {	
    override def tick(running_for: Double, delta: Double) = {
        if (bornTime + 400 < running_for)
            valid = false
    }
}