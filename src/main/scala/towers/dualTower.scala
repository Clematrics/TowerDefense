import java.awt._
import java.awt.geom.{AffineTransform, Point2D}

class DualTower extends RadiusTower(0, 250, 20) {
	cost = 80
	var added: Boolean = false
	
	def getName(): String = {
		return "Dual Tower"
	}

	def tick(running_for: Double, delta: Double) : Unit = {
		if (lastShot + reload < running_for) {
			var enemiesNear : Array[Enemy] = Game.getEnemiesWhere(e => Math.abs(e.pos.x-pos.x) < 1)

			for (e <- enemiesNear) {
				e.takeDamage(pow)
				if (!e.isAlive())
					Game.gold += e.getGold()
			}

			if (enemiesNear.length > 0)
				lastShot = running_for
		}
	}

	override def render(g: Graphics2D): Unit = {
		val s:Image = SpriteLoader.fromResource("dualtour.png")
		val sPos = pos.toScreenPosition
		
		g.setColor(Color.YELLOW)
        g.setStroke(new BasicStroke(10))
        val sa = (pos - new CellPoint(0,8)).toScreenPosition
        val sb = (pos - new CellPoint(0,-8)).toScreenPosition
		g.drawImage(s, new AffineTransform(1, 0, 0, 1, sa.x, sa.y - 40), null)
		g.drawImage(s, new AffineTransform(1, 0, 0, 1, sb.x, sb.y - 40), null)
		g.drawLine(sa.x+30, sa.y, sb.x+30, sb.y)
	}
}