import java.awt._
import java.awt.geom.{AffineTransform, Point2D}

class LaserTower extends RadiusTower(6, 2000, 20) {
	cost = 20

	def getName(): String = {
		return "Laser Tower"
	}

	def tick(running_for: Double, delta: Double) : Unit = {
		if (lastShot + reload < running_for) {
			var enemiesNear : Array[Enemy] = Game.getEnemiesAround(pos, radius)

			if (enemiesNear.length > 0) {
				var e = enemiesNear(0)
				e.takeDamage(pow)
				if (!e.isAlive())
					Game.gold += e.getGold()
				Game.entities += new LaserBeam(running_for, e.asInstanceOf[MovingEnemy], this, Color.RED)
				lastShot = running_for
			}
		}
	}

	override def render(g: Graphics2D): Unit = {
		val s:Image = SpriteLoader.fromResource("lasertour.png")
		val sPos = pos.toScreenPosition
		RenderLayers.groundEntities.drawImage(s, new AffineTransform(1, 0, 0, 1, sPos.x, sPos.y - 15), null)
	}
}