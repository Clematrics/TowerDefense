import engine.core.Renderer
import engine.helpers.{CellPoint, ScreenPoint}
import engine.loaders.SpriteLoader

import java.awt._
import java.awt.geom.AffineTransform

/**
  * An enemy which absorb all light excepted red and blue. We can only see
  * it as a purple sphere.
  */
class SphereEnemy extends MovingEnemy with LivingEnemy {
	gold = 50
	experience = 200
	var damage = 5

	def getName(): String = {
		return "High Dimensional Sphere"
	}

	def takeDamage(dmg: Int): Unit = {
		setLifePoints(lifePoints - dmg)
	}

	override def render(time: Double, delta: Double): Unit = {
		super[MovingEnemy].render(time, delta)
		val sPos = pos.toScreenPosition
		Renderer.groundEntities.setColor(new Color(80, 20, 100, 255))
		Renderer.groundEntities.fillOval(sPos.x - 20, sPos.y - 20, 40, 40)
		drawHealthBar(sPos + new ScreenPoint(0, -30))
	}
}