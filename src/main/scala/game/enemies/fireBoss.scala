import engine.core.Renderer
import engine.helpers.{CellPoint, ScreenPoint}
import engine.loaders.SpriteLoader
import java.awt.geom.AffineTransform
import java.awt._

/**
  * A boss. 
  * In the future : make boss a trait to define what all boss-enemies do, such
  * as recovery, double life, etc.
  */
class FireBoss extends MovingEnemy with LivingEnemy {
	speed = 0.6
	gold = 200
	experience = 1000

	/**
  	 * The extra protection for this enemy.
  	 */
	var armorPoints:Int = 100
	def getName(): String = {
		return "Fire Boss"
	}

	def takeDamage(dmg: Int): Unit = {
		if (armorPoints <= 0) {
			setLifePoints(lifePoints - (if (lifePoints <= 20) 1 else dmg))//Special resistance
		} else {
			armorPoints -= dmg / 2 //Armor resistance
		}
	}

	def getGold(): Int = {
		return gold
	}

	override def drawHealthBar(p: ScreenPoint) = {
		Renderer.userInterface.setColor(Color.BLACK)
		Renderer.userInterface.fillRect(p.x - 50, p.y, 100, 7)
		Renderer.userInterface.setColor(if (armorPoints > 0) Color.MAGENTA else Color.RED)
		Renderer.userInterface.fillRect(p.x - 49, p.y + 1, lifePoints / 2 + armorPoints / 2 - 2, 5)
	}

	val regenDelay: Double = 2000.0
	var lastRegen: Double = 0.0

	def render(time: Double, delta: Double): Unit = {
		val s:Image = SpriteLoader.fromResource("anim/boss" + (time/300 % 12).toInt +".png")
		val sPos = pos.toScreenPosition
		//TODO : manage directions?
		Renderer.groundEntities.drawImage(s, new AffineTransform(-0.4, 0, 0, 0.4, sPos.x + 80, sPos.y  - 40), null)
		drawHealthBar(sPos + new ScreenPoint(0, -30))
	
		if (lifePoints < 75 && time - lastRegen > regenDelay) {
			setLifePoints(lifePoints + 5)
			lastRegen = time
			speed = speed * 1.5
		}
	}
}