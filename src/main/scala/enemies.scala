import java.awt.Graphics2D
import java.awt.Color
import java.awt.geom.Point2D

abstract class LivingEnemy(life: Int) extends Enemy {
	var lifePoints = life

	def isAlive(): Boolean = {
		return lifePoints <= 0
	}

	def drawHealthBar(g: Graphics2D, p: ScreenPosition) = {
		g.setColor(Color.DARK_GRAY)
		g.fillRoundRect(p.x - 50, p.y, 100, 10, 10, 10)
		g.setColor(Color.RED)
		g.fillRoundRect(p.x - 50, p.y, lifePoints, 10, 10, 10)
	}
}

class SphereEnemy extends LivingEnemy(100) {
	var pos: CellPosition = new CellPosition(10, 10)

	def getName(): String = {
		return "High Dimensional Sphere"
	}

	def takeDamage(dmg: Int): Unit = {
		lifePoints -= dmg
	}

	def tick(running_for: Double, delta: Double) : Unit = {
	}

	override def render(g: Graphics2D): Unit = {
		val sPos = pos.toScreenPosition
		g.setColor(new Color(80, 20, 100, 255))
		g.fillOval(sPos.x - 40, sPos.y - 40, 80, 80)
		drawHealthBar(g, sPos + new ScreenPosition(0, -60))
	}
}