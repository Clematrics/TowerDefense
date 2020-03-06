import java.awt.Graphics2D
import scala.swing.Publisher
import scala.swing.event.KeyTyped

/**
  * Abstract class representing a level of the game
  */
abstract class View extends Publisher {
	reactions += {
		case KeyTyped(_, 'd', _, _) =>
			RenderLayers.debugMode = !RenderLayers.debugMode
	}

	def tick(running_for: Double, delta: Double) {}
	def render(g: Graphics2D, running_for: Double, delta: Double)
}