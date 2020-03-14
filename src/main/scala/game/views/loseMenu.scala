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
class LoseMenu extends View { outer =>
	buttons ++= ArrayBuffer(
		new Button(new Point(320, 210), new Dimension(400, 80)) {
			spriteBack    = SpriteLoader.fromResource("menuButtonLarge.png")
			spriteFront   = SpriteLoader.fromString("Retry", 400, 35)
			action = () => {
				Game.reset
				GamePanel.changeView("DefensePhase")
			}
		}
	)

	def render(time: Double, delta: Double): Unit = {
		val loseImg = SpriteLoader.fromString("You lose!", 400, 35)
		Renderer.userInterface.drawImage(loseImg, new AffineTransform(1, 0, 0, 1, 320 - loseImg.getWidth(null) / 2, 110), null)

		for(b <- buttons) {
			b.render(time, delta)
		}
	}
}