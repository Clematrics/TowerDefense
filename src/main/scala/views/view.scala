import java.awt.Graphics2D
import scala.swing.Publisher
import scala.swing.event.KeyTyped
import scala.collection.mutable.ArrayBuffer
import scala.swing.event.MouseMoved
import scala.swing.event.MouseReleased

/**
  * Abstract class representing a level of the game
  */
abstract class View extends Publisher {
	reactions += {
		case MouseMoved(_, point, _) =>
			for(b <- buttons) b.onMoved(point)
		case MouseReleased(_, point, _, _, _) =>
			for(b <- buttons) b.onRelease(point)
		case KeyTyped(_, 'd', _, _) =>
			Renderer.debugMode = !Renderer.debugMode
	}

	var buttons: ArrayBuffer[Button] = ArrayBuffer()

	def tick(running_for: Double, delta: Double) {}
	def render(g: Graphics2D, running_for: Double, delta: Double)
}