import engine.core.Renderer
import engine.helpers.{CellPoint, ScreenPoint}
import engine.loaders.SpriteLoader
import java.awt.geom.AffineTransform
import java.awt._

/**
  * The enemy for the demonstration of an animation.
  *
  */
class FlyingEnemy extends MovingEnemy with LivingEnemy {
	speed = 0.1
  gold = 80
	experience = 400

	def getName(): String = {
		return "Dragon"
	}

	def takeDamage(dmg: Int): Unit = {
		setLifePoints(lifePoints - dmg)
	}

	def getGold(): Int = {
		return gold
	}

	def render(time: Double, delta: Double): Unit = {
		val s:Image = SpriteLoader.fromResource("anim/dragon" + (time/100 % 11).toInt +".png")
		val sPos = pos.toScreenPosition
		//TODO : manage directions?
		Renderer.groundEntities.drawImage(s, new AffineTransform(-0.4, 0, 0, 0.4, sPos.x + 35, sPos.y  - 15), null)
		drawHealthBar(sPos + new ScreenPoint(0, -30))
	}
}