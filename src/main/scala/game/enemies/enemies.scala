import engine.core.Renderer
import engine.helpers.{CellPoint, ScreenPoint}
import engine.loaders.SpriteLoader

import java.awt._
import scala.math.{cos, sin, atan}
import java.awt.geom.AffineTransform

/**
  * Living enemies all have 100 HP (health points). The manner
  * they react to attacks is determined by the takeDamage procedure
  * of the concrete enemies classes.
  */
trait LivingEnemy extends Enemy {
	var lifePoints = 100

	def isAlive(): Boolean = {
		return lifePoints >= 0
	}

	def setLifePoints(lp: Int) = {
		lifePoints = lp
		if (lifePoints <= 0) {
			valid = false
		}
	}

	def drawHealthBar(p: ScreenPoint) = {
		Renderer.userInterface.setColor(Color.BLACK)
		Renderer.userInterface.fillRect(p.x + 25 - lifePoints / 2, p.y, lifePoints / 2, 10)
		Renderer.userInterface.setColor(Color.RED)
		Renderer.userInterface.fillRect(p.x - 25, p.y, lifePoints /  2, 10)
	}
}

/**
  * The MovingEnemy trait represents an enemy with a speed that can
  * move across the board.
  */
trait MovingEnemy extends Enemy {
	var speed: Double = 0.04
	var targetedCheckpoint: Int = -2
	var targetedCellPoint: CellPoint = new CellPoint(0, 0)
	var pos: CellPoint = new CellPoint(0, 0)

	def tick(time: Double, delta: Double) : Unit = {
		var cp = Game.map.checkpoints(targetedCheckpoint)
		if (pos.distance(targetedCellPoint) <= speed) {
			targetedCheckpoint = cp.next
			if (targetedCheckpoint == -1) {
				Game.health = (Game.health - 20) max 0
				valid = false
				return
			}
			cp = Game.map.checkpoints(targetedCheckpoint)
			val r = scala.util.Random
			targetedCellPoint = cp.a + new CellPoint(r.nextFloat, r.nextFloat) * (cp.b - cp.a)
		}

		val dist = pos.distance(targetedCellPoint)
		val dirx = (targetedCellPoint.x - pos.x) / dist
		val diry = (targetedCellPoint.y - pos.y) / dist
		pos += new CellPoint(dirx, diry) * speed
	}
}

/**
  * An enemy which absorb all light excepted red and blue. We can only see
  * it as a purple sphere.
  */
class SphereEnemy extends MovingEnemy with LivingEnemy {
	var gold:Int = 200

	def getName(): String = {
		return "High Dimensional Sphere"
	}

	def takeDamage(dmg: Int): Unit = {
		setLifePoints(lifePoints - dmg)
	}

	def getGold(): Int = {
		return 5 // Constant reward
	}

	def render(time: Double, delta: Double): Unit = {
		val sPos = pos.toScreenPosition
		Renderer.groundEntities.setColor(new Color(80, 20, 100, 255))
		Renderer.groundEntities.fillOval(sPos.x - 20, sPos.y - 20, 40, 40)
		drawHealthBar(sPos + new ScreenPoint(0, -30))
	}
}

/**
  * The enemy designed for development purposes.
  *
  */
class ProtoEnemy extends MovingEnemy with LivingEnemy {
	speed = 0.02
	var gold:Int = 500

	def getName(): String = {
		return "Chess pawn"
	}

	def takeDamage(dmg: Int): Unit = {
		setLifePoints(lifePoints - dmg)
	}

	def getGold(): Int = {
		return scala.util.Random.nextInt(15) // Random reward
	}

	def render(time: Double, delta: Double): Unit = {
		val s:Image = SpriteLoader.fromResource("pion.png")
		val sPos = pos.toScreenPosition
		Renderer.groundEntities.drawImage(s, new AffineTransform(0.1, 0, 0, 0.1, sPos.x - 20, sPos.y  - 20), null)
		drawHealthBar(sPos + new ScreenPoint(0, -30))
	}
}