package engine.interaction

import engine.core.Renderer
import engine.Cst
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
class Button(var position: Point, var size: Dimension, var isText: Boolean = true) extends Reactor {
	var spriteBack: Image = null
	var spriteFront: Image = null
	var spriteTooltip: Image = null
	var borderTop = position.y - size.getHeight / 2
	var borderBottom = position.y + size.getHeight / 2
	var borderLeft = position.x - size.getWidth / 2
	var borderRight = position.x + size.getWidth / 2

	private var cursorInside = false
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
		if (spriteBack != null) {
			val scaleX = size.getWidth / spriteBack.getWidth(null)
			val scaleY = size.getHeight / spriteBack.getHeight(null)
			Renderer.userInterface.drawImage(spriteBack, new AffineTransform(scaleX, 0, 0, scaleY, borderLeft, borderTop), null)
		}
		if (spriteFront != null) {
			val hgt = spriteFront.getHeight(null)

			var scale: Double = 1
			var offsetX = 0
			var offsetY = 0

			if (isText) { //TODO : ne pas faire dépasser le texte !
				offsetX = spriteFront.getWidth(null) / 2
				offsetY = hgt / 2
				Renderer.text.drawImage(spriteFront, new AffineTransform(scale
					, 0, 0, scale,
					Cst.textLayerScaling * position.x - offsetX, Cst.textLayerScaling * position.y - offsetY), null)
			} else {
				if (hgt > size.getHeight()) {
					scale = size.getHeight / hgt
					offsetX = (spriteFront.getWidth(null) * scale / 2).toInt
					offsetY = (size.getHeight / 2).toInt
				} else {
					offsetX = spriteFront.getWidth(null) / 2
					offsetY = hgt / 2
				}
				Renderer.userInterface.drawImage(spriteFront, new AffineTransform(scale, 0, 0, scale, position.x - offsetX, position.y - offsetY), null)
			}
		}
		if (cursorInside && spriteTooltip != null) {
			Renderer.text.drawImage(spriteTooltip, new AffineTransform(1, 0, 0, 1,
				engine.Cst.textLayerScaling * (0.0 max (mousePosition.getX - spriteTooltip.getWidth(null) / engine.Cst.textLayerScaling)),
				engine.Cst.textLayerScaling * (0.0 max (mousePosition.getY - spriteTooltip.getHeight(null) / engine.Cst.textLayerScaling))), null)
		}
	}
}