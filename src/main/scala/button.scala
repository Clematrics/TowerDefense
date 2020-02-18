import java.awt.{Dimension, Graphics2D, Image, Point}
import scala.swing.Reactor
import java.awt.geom.AffineTransform

class Button(p: Point, d: Dimension) extends Reactor {
	var sprite_back:  Image = null
	var sprite_front: Image = null
	var position: Point     = p
	var size: Dimension     = d
	var border_top    = position.getY - size.getHeight / 2
	var border_bottom = position.getY + size.getHeight / 2
	var border_left   = position.getX - size.getWidth  / 2
	var border_right  = position.getX + size.getWidth  / 2

	private var cursorInside = false
	private def isInside(point: Point): Boolean = {
		return border_left <= point.getX && point.getX <= border_right && border_top <= point.getY && point.getY <= border_bottom
	}

	def onRelease(point: Point) {
		if (isInside(point))
			action()
	}
	def onMoved(point: Point) {
		cursorInside = isInside(point)
	}

	var action = () => {}
	def render(g: Graphics2D, time: Double, delta: Double) = {
		if (cursorInside) {
			// TODO draw tooltip
		}
		for(sprite <- List(sprite_back, sprite_front)) {
			val scaleX = size.getWidth / sprite.getWidth(null)
			val scaleY = size.getHeight / sprite.getHeight(null)
			g.drawImage(sprite, new AffineTransform(scaleX, 0, 0, scaleY, border_left, border_top), null)
		}
		// TODO draw button
	}
}