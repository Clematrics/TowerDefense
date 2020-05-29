import engine.core.{GamePanel, Renderer, View}
import engine.loaders.SpriteLoader
import engine.interaction.Button

import scala.swing.event._
import java.awt.{Color, Dimension, Graphics2D, Point}
import java.awt.geom.AffineTransform
import scala.collection.mutable._

/**
  * This window is visible in case of fatal network error.
  *
  */
class NetworkErrorMenu extends View {
	outer =>
	buttons ++= ArrayBuffer(
		new Button(new Point(320, 210), new Dimension(400, 80)) {
			spriteBack = SpriteLoader.fromResource("button400x80.png")
			spriteFront = SpriteLoader.fromString("Go back to main menu", 400, 35)
			action = () => {
				Game.reset
				GamePanel.changeView("MainMenu")
			}
		}
	)

	def render(time: Double, delta: Double): Unit = {
		val winImg = SpriteLoader.fromString("Something went wrong : bad signal received", 400, 35)
		Renderer.drawOnTextLayerCentered(winImg, 320, 110)

		for (b <- buttons) {
			b.render(time, delta)
		}
	}
}