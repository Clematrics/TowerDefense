import engine.core.{GamePanel, Renderer, View}
import engine.loaders.SpriteLoader
import engine.interaction.Button

import scala.swing.event._
import java.awt.{Color, Dimension, Graphics2D, Point}
import java.awt.geom.AffineTransform
import scala.collection.mutable._

/**
  * This window is visible when the player wins.
  *
  */
class WinMenu extends View { outer =>
	buttons ++= ArrayBuffer(
		new Button(new Point(320, 210), new Dimension(400, 80)) {
			spriteBack    = SpriteLoader.fromResource("menuButtonLarge.png")
			spriteFront   = SpriteLoader.fromString("Explode more monsters", 400, 35)
			action = () => {
				Game.reset
				GamePanel.changeView("CampaignMenu")
			}
		}
	)

	def render(time: Double, delta: Double): Unit = {
		val winImg = SpriteLoader.fromString("You win!", 400, 35)
		Renderer.userInterface.drawImage(winImg, new AffineTransform(1, 0, 0, 1, 320 - winImg.getWidth(null) / 2, 110), null)

		for(b <- buttons) {
			b.render(time, delta)
		}
	}
}