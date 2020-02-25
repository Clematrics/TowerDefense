import java.awt.Graphics2D
import java.awt.BasicStroke
import java.awt.Color

class Particle(me: MovingEnemy, t: Tower) extends Entity {
    var target = me
    var pos = new CellPoint(t.pos)
    val speed = 0.2
    val dmg = 10

    def tick(running_for: Double, delta: Double): Unit = {
        val dist = pos.distance(target.pos)
        if (dist <= speed) {
            valid = false
            target.takeDamage(dmg)
            return
        }
		val dirx = (target.pos.x - pos.x) / dist
		val diry = (target.pos.y - pos.y) / dist
		pos += new CellPoint(dirx, diry) * speed
    }

    def render(g: Graphics2D, running_for: Double, delta: Double) = {
        g.setColor(Color.PINK)
        g.setStroke(new BasicStroke(10))
        val sp = pos.toScreenPosition
        g.drawOval(sp.x - 5, sp.y - 5, 10, 10)
    }
}