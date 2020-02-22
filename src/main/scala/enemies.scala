import java.awt.{Color, Dimension, Graphics2D, Point, Image}
import java.awt.geom.{AffineTransform, Point2D}

abstract class LivingEnemy(life: Int) extends Enemy {
	var lifePoints = life

	def isAlive(): Boolean = {
		return lifePoints >= 0
	}

	def drawHealthBar(g: Graphics2D, p: ScreenPosition) = {
		g.setColor(Color.BLACK)
		g.fillRect(p.x - 51, p.y - 1, 102, 12)
		g.setColor(Color.RED)
		g.fillRect(p.x - 50, p.y, lifePoints, 10)
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

class ProtoEnemy extends LivingEnemy(75) {
	var pos: CellPosition = new CellPosition(10, 10)

	def getName(): String = {
		return "Chess pawn"
	}

	def takeDamage(dmg: Int): Unit = {
		lifePoints -= dmg
	}

	def tick(running_for: Double, delta: Double) : Unit = {
		
	}

	override def render(g: Graphics2D): Unit = {
		/*val sPos = pos.toScreenPosition
		g.setColor(new Color(80, 255, 255, 0))
		g.fillRect(sPos.x-20, sPos.y-20, 40, 40)
		drawHealthBar(g, sPos + new ScreenPosition(0, -60))*/
		val s:Image = SpriteLoader.fromResource("pion.png")
		val sPos = pos.toScreenPosition
		g.drawImage(s, new AffineTransform(0.2, 0, 0, 0.2, sPos.x-40, sPos.y-40), null)
		drawHealthBar(g, sPos + new ScreenPosition(0, -60))
	
	}
}