import java.awt._
import java.awt.geom.{AffineTransform, Point2D}

class MultiTower extends RadiusTower(7, 2000, 20) {
	cost = 40
	def getName(): String = {
		return "Multi Tower"
	}

	def tick(running_for: Double, delta: Double) : Unit = {
		if (lastShot + reload < running_for) {
			var enemiesNear : Array[Enemy] = Game.getEnemiesAround(pos, radius)

			for (e <- enemiesNear) {
				e.takeDamage(pow)
				if (!e.isAlive())
					Game.gold += e.getGold()
				Game.entities += new LaserBeam(running_for, e.asInstanceOf[MovingEnemy], this, Color.MAGENTA)
			}

			if (enemiesNear.length > 0)
				lastShot = running_for
		}
	}

	def render(running_for: Double, delta: Double): Unit = {
		val s:Image = SpriteLoader.fromResource("multitour.png")
		val sPos = pos.toScreenPosition
		Renderer.groundEntities.drawImage(s, new AffineTransform(1, 0, 0, 1, sPos.x, sPos.y - 40), null)
	}
}