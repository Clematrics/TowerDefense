import engine.core.Renderer
import engine.helpers.{CellPoint, ScreenPoint}
import engine.loaders.SpriteLoader
import java.awt._

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
			if (valid) {
				Game.gold += gold
				Game.experience += experience
			}
			valid = false
		}
	}

	def drawHealthBar(p: ScreenPoint) = {
		Renderer.userInterface.setColor(Color.BLACK)
		Renderer.userInterface.fillRect(p.x - 25, p.y, 50, 7)
		Renderer.userInterface.setColor(Color.RED)
		Renderer.userInterface.fillRect(p.x - 24, p.y + 1, lifePoints /  2 - 2, 5)
	}
}