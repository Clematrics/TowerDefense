import engine.core.Renderer
import engine.helpers.{CellPoint, ScreenPoint}
import engine.loaders.SpriteLoader
import java.awt.geom.AffineTransform
import java.awt._

/**
  * The enemy designed for development purposes.
  *
  */
class ProtoEnemy extends MovingEnemy with LivingEnemy {
	speed = 1.2
	gold = 20
	experience = 100
	var damage = 10

	def getName(): String = {
		return "Chess pawn"
	}

	def takeDamage(dmg: Int): Unit = {
		setLifePoints(lifePoints - dmg)
	}

	override def render(time: Double, delta: Double): Unit = {
		super[MovingEnemy].render(time, delta)
		val s:Image = SpriteLoader.fromResource("pion.png")
		val sPos = pos.toScreenPosition
		Renderer.groundEntities.drawImage(s, new AffineTransform(0.1, 0, 0, 0.1, sPos.x - 20, sPos.y  - 20), null)
		drawHealthBar(sPos + new ScreenPoint(0, -30))
	}
}