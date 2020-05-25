import engine.core.{GamePanel, Renderer, View}
import engine.loaders.SpriteLoader
import engine.interaction.Button

import scala.swing.event._
import java.awt.{Color, Dimension, Graphics2D, Point}
import java.awt.geom.AffineTransform
import scala.collection.mutable.ArrayBuffer

/**
  * The window to retry another game when the player loses.
  *
  */
class LoseMenu extends View {
	outer =>
	buttons ++= ArrayBuffer(
		new Button(new Point(320, 210), new Dimension(400, 80)) {
			spriteBack = SpriteLoader.fromResource("button400x80.png")
			spriteFront = SpriteLoader.fromString("Retry", 400, 35)
			action = () => {
				Game.reset
				GamePanel.changeView("DefensePhase")
			}
		}
	)

	def render(time: Double, delta: Double): Unit = {
		val loseImg = SpriteLoader.fromString("You lose!", 400, 35)
		Renderer.drawOnTextLayerCentered(loseImg, 320, 110)

		for (b <- buttons) {
			b.render(time, delta)
		}
	}
}