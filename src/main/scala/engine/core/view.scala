package engine.core

import engine.interaction.{Button, MouseHelper}

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
	var buttons: ArrayBuffer[Button] = ArrayBuffer()

	reactions += {
		case MouseMoved(_, point, _) =>
			for(b <- buttons) b.onMoved(MouseHelper.fromMouse(point))
		case MouseReleased(_, point, _, _, _) =>
			if (point != null) for(b <- buttons) b.onRelease(MouseHelper.fromMouse(point))
		case KeyTyped(_, 'd', _, _) =>
			Renderer.debugMode = !Renderer.debugMode
	}

	def tick(time: Double, delta: Double) {}
	def render(time: Double, delta: Double)
}

class DefaultView extends View {
	def render(time: Double, delta: Double): Unit = {}
}