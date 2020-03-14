import java.awt.Graphics2D
import java.awt.BasicStroke
import java.awt.Color

/**
  * Entity embodying particles shot towards enemies.
  *
  * @param me	The targeted enemy
  * @param t	Source tower
  */
class Particle(me: MovingEnemy, t: Tower) extends Entity {
    var target = me
    var pos = new CellPoint(t.pos)
    val speed = 0.2
    val dmg = 10

    def tick(time: Double, delta: Double): Unit = {
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

	def render(time: Double, delta: Double) = {
        Renderer.flyingEntities.setColor(Color.PINK)
        Renderer.flyingEntities.setStroke(new BasicStroke(10))
        val sp = pos.toScreenPosition
        Renderer.flyingEntities.drawOval(sp.x - 5, sp.y - 5, 10, 10)
    }
}