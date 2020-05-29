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
	speed = 6.0
	gold = 80
	experience = 400
	var damage = 20

	def getName(): String = {
		return "Dragon"
	}

	def takeDamage(dmg: Int): Unit = {
		setLifePoints(lifePoints - dmg)
	}

	override def render(time: Double, delta: Double): Unit = {
		super[MovingEnemy].render(time, delta)
		val s:Image = SpriteLoader.fromResource("anim/dragon" + (time/100 % 11).toInt +".png")
		val sPos = pos.toScreenPosition
		//TODO : manage directions?
		Renderer.flyingEntities.drawImage(s, new AffineTransform(-0.4, 0, 0, 0.4, sPos.x + 35, sPos.y  - 15), null)
		drawHealthBar(sPos + new ScreenPoint(0, -30))
	}
}