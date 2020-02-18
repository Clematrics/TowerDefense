import java.awt.Point
import java.awt.Dimension
import java.awt.Graphics2D
import java.awt.Image
import scala.swing.Reactor
import scala.swing.event.MouseClicked
import scala.swing.event.MouseMoved
import java.awt.geom.AffineTransform

abstract class Button extends Reactor {
	var sprite: Image
	var position: Point
	var size: Dimension

	private var cursorInside = false
	private def isInside(point: Point): Boolean = {
		return position.getX <= point.getX && point.getX <= position.getX + size.getWidth && position.getY <= point.getY && point.getY <= position.getY + size.getHeight
	}

	reactions += {
		case MouseClicked(_, point, _, _, _) => {
			if (isInside(point))
				action()
		}
		case MouseMoved(_, point, _) => {
			cursorInside = isInside(point)
		}
	}

	var action = () => {}
	def render(g: Graphics2D, time: Double, delta: Double) = {
		if (cursorInside) {
			// TODO draw tooltip
		}
		val scaleX = size.getWidth / sprite.getWidth(null)
		val scaleY = size.getHeight / sprite.getHeight(null)
		g.drawImage(sprite, new AffineTransform(scaleX, 0, 0, scaleY, position.getX, position.getY), null)
		// TODO draw button
	}
}