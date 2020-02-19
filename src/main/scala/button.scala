import java.awt.{Dimension, Graphics2D, Image, Point}
import scala.swing.Reactor
import java.awt.geom.AffineTransform

class Button(p: Point, d: Dimension) extends Reactor {
	var sprite_back:  Image   = null
	var sprite_front: Image   = null
	var sprite_tooltip: Image = null
	var position: Point       = p
	var size: Dimension       = d
	var borderTop    = position.getY - size.getHeight / 2
	var borderBottom = position.getY + size.getHeight / 2
	var borderLeft   = position.getX - size.getWidth  / 2
	var borderRight  = position.getX + size.getWidth  / 2

	private var cursorInside  = false
	private var mousePosition = new Point(0, 0)
	private def isInside(point: Point): Boolean = {
		return borderLeft <= point.getX && point.getX <= borderRight && borderTop <= point.getY && point.getY <= borderBottom
	}

	def onRelease(point: Point) {
		if (isInside(point))
			action()
	}
	def onMoved(point: Point) {
		cursorInside = isInside(point)
		mousePosition = point
	}

	var action = () => {}
	def render(g: Graphics2D, time: Double, delta: Double) = {
		for(sprite <- List(sprite_back, sprite_front)) {
			if (sprite != null) {
				val scaleX = size.getWidth / sprite.getWidth(null)
				val scaleY = size.getHeight / sprite.getHeight(null)
				g.drawImage(sprite, new AffineTransform(scaleX, 0, 0, scaleY, borderLeft, borderTop), null)
			}
		}
		if (cursorInside && sprite_tooltip != null) {
			g.drawImage(sprite_tooltip, new AffineTransform(1, 0, 0, 1, mousePosition.getX - sprite_tooltip.getWidth(null), mousePosition.getY - sprite_tooltip.getHeight(null)), null)
		}
		// TODO draw button border, animation, ...
	}
}