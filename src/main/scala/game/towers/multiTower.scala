import engine.core.Renderer
import engine.loaders.SpriteLoader

import java.awt._
import java.awt.geom.{AffineTransform, Point2D}

/**
  * This class represents a tower able to shoot at
  * several enemies at a time.
  */
class MultiTower extends RadiusTower(7, 2000, 20) {
	cost = 40
	def getName(): String = {
		return "Multi Tower"
	}

	def tick(time: Double, delta: Double) : Unit = {
		if (lastShot + reload < time) {
			var enemiesNear : Array[Enemy] = Game.getEnemiesAround(pos, radius)

			for (e <- enemiesNear) {
				e.takeDamage(pow)
				if (!e.isAlive())
					Game.gold += e.getGold()
				Game.entities += new LaserBeam(time, e.asInstanceOf[MovingEnemy], this, Color.MAGENTA)
			}

			if (enemiesNear.length > 0)
				lastShot = time
		}
	}

	def render(time: Double, delta: Double): Unit = {
		val s:Image = SpriteLoader.fromResource("multitour.png")
		val sPos = pos.toScreenPosition
		Renderer.groundEntities.drawImage(s, new AffineTransform(1, 0, 0, 1, sPos.x, sPos.y - 40), null)
	}
}