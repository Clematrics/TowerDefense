import engine.core.Renderer
import engine.loaders.SpriteLoader

import java.awt._
import java.awt.geom.{AffineTransform, Point2D}

/**
  * A tower shooting enemies
  *
  */
class ArmedTower extends RadiusTower(6, 1000, 5) {
	cost = 10
	def getName(): String = {
		return "Armed Tower"
	}

	def tick(time: Double, delta: Double) : Unit = {
		if (lastShot + reload < time) {
			var enemiesNear : Array[Enemy] = Game.getEnemiesAround(pos, radius)

			if (enemiesNear.length > 0) {
				var e = enemiesNear(0)
				e.takeDamage(pow)
				if (!e.isAlive())
					Game.gold += e.getGold()
				Game.entities += new Particle(e.asInstanceOf[MovingEnemy], this)
				lastShot = time
			}
		}
	}

	def render(time: Double, delta: Double): Unit = {
		val s:Image = SpriteLoader.fromResource("armedtour.png")
		val sPos = pos.toScreenPosition
		Renderer.groundEntities.drawImage(s, new AffineTransform(1, 0, 0, 1, sPos.x, sPos.y - 40), null)
	}
}