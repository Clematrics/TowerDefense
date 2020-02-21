import java.awt.Graphics2D
import java.awt.Color
class SphereEnemy extends Enemy {
	var lifePoints = 100

	var x, y: Float = 10

	def isAlive(): Boolean = {
		return lifePoints <= 0
	}

	def getName(): String = {
		return "High Dimensional Sphere"
	}

	def takeDamage(dmg: Int): Unit = {
		lifePoints -= dmg
	}

	def update(): Unit = {
	}

	override def render(g: Graphics2D): Unit = {
		val u = (x * 24).toInt
		val v = (y * 24).toInt
		g.setColor(new Color(80, 20, 100, 255))
		g.fillOval(u - 40, v.toInt - 40, 80, 80)
		g.setColor(Color.DARK_GRAY)
		g.fillRoundRect(u - 60, v - 60, 120, 10, 10, 10)
		g.setColor(Color.RED)
		g.fillRoundRect(u - 60, v - 60, 120 * lifePoints / 100, 10, 10, 10)
	}
}