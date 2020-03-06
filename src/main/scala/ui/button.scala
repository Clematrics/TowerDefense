import java.awt.{Dimension, Graphics2D, Image, Point}
import scala.swing.Reactor
import java.awt.geom.AffineTransform

/**
  * The class Button implements the visual item that the player
  * can press to go to another level or to perform different actions.
  *
  * @param p Location of the button
  * @param d Size of the button
  */
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
	def render(time: Double, delta: Double) = {
		if (sprite_back != null) {
			val scaleX = size.getWidth / sprite_back.getWidth(null)
			val scaleY = size.getHeight / sprite_back.getHeight(null)
			Renderer.userInterface.drawImage(sprite_back, new AffineTransform(scaleX, 0, 0, scaleY, borderLeft, borderTop), null)
		}
		if (sprite_front != null) {
			val offsetX = sprite_front.getWidth(null) / 2
			val offsetY = sprite_front.getHeight(null) / 2
			Renderer.userInterface.drawImage(sprite_front, new AffineTransform(1, 0, 0, 1, position.x - offsetX, position.y - offsetY), null)
		}
		if (cursorInside && sprite_tooltip != null) {
			Renderer.userInterface.drawImage(sprite_tooltip, new AffineTransform(1, 0, 0, 1, mousePosition.getX - sprite_tooltip.getWidth(null), mousePosition.getY - sprite_tooltip.getHeight(null)), null)
		}
	}
}