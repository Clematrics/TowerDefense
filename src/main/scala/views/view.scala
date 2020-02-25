import java.awt.Graphics2D
import scala.swing.Publisher
import scala.swing.event.KeyTyped

/**
	* Abstract class representing a level of the game
	*/
abstract class View extends Publisher {
	var debugMode = false
	reactions += {
		case KeyTyped(_, 'd', _, _) =>
			debugMode = !debugMode
	}

	def tick(running_for: Double, delta: Double) {}
	def render(g: Graphics2D, running_for: Double, delta: Double)
}