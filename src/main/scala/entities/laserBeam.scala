import java.awt.Graphics2D
import java.awt.BasicStroke
import java.awt.Color

class LaserBeam(d: Double, me: MovingEnemy, t: Tower, c: Color) extends Entity {
    var a: CellPoint = me.pos
    var b: CellPoint = t.pos
	var bornTime: Double = d
	var clr: Color = c

    override def tick(running_for: Double, delta: Double) = {
        if (bornTime + 400 < running_for)
            valid = false
    }

    override def render(g: Graphics2D) = {
        RenderLayers.flyingEntities.setColor(clr)
        RenderLayers.flyingEntities.setStroke(new BasicStroke(10))
        val sa = a.toScreenPosition
        val sb = b.toScreenPosition
		RenderLayers.flyingEntities.drawLine(sa.x, sa.y, sb.x, sb.y)
    }
}