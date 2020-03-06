import java.awt._
import java.awt.geom.{AffineTransform, Point2D}

class LaserTower extends RadiusTower(6, 2000, 20) {
	cost = 20

	def getName(): String = {
		return "Laser Tower"
	}

	def tick(time: Double, delta: Double) : Unit = {
		if (lastShot + reload < time) {
			var enemiesNear : Array[Enemy] = Game.getEnemiesAround(pos, radius)

			if (enemiesNear.length > 0) {
				var e = enemiesNear(0)
				e.takeDamage(pow)
				if (!e.isAlive())
					Game.gold += e.getGold()
				Game.entities += new LaserBeam(time, e.asInstanceOf[MovingEnemy], this, Color.RED)
				lastShot = time
			}
		}
	}

	def render(time: Double, delta: Double): Unit = {
		val s:Image = SpriteLoader.fromResource("lasertour.png")
		val sPos = pos.toScreenPosition
		Renderer.groundEntities.drawImage(s, new AffineTransform(1, 0, 0, 1, sPos.x, sPos.y - 15), null)
	}
}