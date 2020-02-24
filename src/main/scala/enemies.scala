import java.awt._
import java.awt.geom.{AffineTransform, Point2D}
import scala.math.{cos, sin, atan}

/**
  * Living enemies all have 100 HP (health points). The manner
  * they react to attacks is determined by the takeDamage procedure
  * of the concrete enemies classes.
  */
abstract class LivingEnemy extends Enemy {
	var lifePoints = 100

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

/**
  * The MovingEnemy class represents an enemy with a speed that can
  * move across the board. It naturally extends LivingEnemy as in order
  * to move, the enemy must be alive. 
  *
  * @param speed_coef The speed coefficient of the enemy. Each tick of the clock (60 times per second or so), the enemy will go further to this unit of distance.
  */
abstract class MovingEnemy(speed_coef: Double) extends LivingEnemy {
	var speed = speed_coef
	
	def tick(running_for: Double, delta: Double) : Unit = {
		var cp = GameStatus.map.checkpoints(targetedCheckpoint)
		if (pos.distance(targetedCellPoint) <= speed) {
			targetedCheckpoint = cp.next
			if (targetedCheckpoint == -1) {
				valid = false
				return
			}
			cp = GameStatus.map.checkpoints(targetedCheckpoint)
			val r = scala.util.Random
			targetedCellPoint = new CellPosition(cp.aX + r.nextFloat * (cp.bX - cp.aX), cp.aY + r.nextFloat * (cp.bY - cp.aY))
		}
		// println(f"Sphere/Proto $targetedCheckpoint, $targetedCellPoint")

		val theta = atan((targetedCellPoint.y - pos.y) / (targetedCellPoint.x - pos.x))
		pos.move(speed * cos(theta), speed * sin(theta))
	}
}

class SphereEnemy extends MovingEnemy(0.01) {
	var gold:Int = 200
	
	def getName(): String = {
		return "High Dimensional Sphere"
	}

	def takeDamage(dmg: Int): Unit = {
		lifePoints -= dmg
	}	

	override def render(g: Graphics2D): Unit = {
		val sPos = pos.toScreenPosition
		g.setColor(new Color(80, 20, 100, 255))
		g.fillOval(sPos.x - 40, sPos.y - 40, 80, 80)
		drawHealthBar(g, sPos + new ScreenPosition(0, -60))
	}
}

class ProtoEnemy extends MovingEnemy(0.02) {
	var gold:Int = 500

	def getName(): String = {
		return "Chess pawn"
	}

	def takeDamage(dmg: Int): Unit = {
		lifePoints -= dmg
	}

	override def render(g: Graphics2D): Unit = {
		val s:Image = SpriteLoader.fromResource("pion.png")
		val sPos = pos.toScreenPosition
		g.drawImage(s, new AffineTransform(0.2, 0, 0, 0.2, sPos.x-40, sPos.y-40), null)
		drawHealthBar(g, sPos + new ScreenPosition(0, -60))
	
	}
}