import engine.core.{GamePanel, Renderer, View}
import engine.loaders.SpriteLoader
import engine.interaction.Button

import scala.swing.event._
import java.awt.{Dimension, Graphics2D, Point}
import scala.collection.mutable.ArrayBuffer

/**
  * This class handles the startup menu of this application.
  */
class MainMenu extends View { outer =>
	buttons ++= ArrayBuffer(
		new Button(new Point(640, 200), new Dimension(800, 160)) {
			spriteBack    = SpriteLoader.fromResource("menuButtonLarge.png")
			spriteFront   = SpriteLoader.fromString("play & prove yourself !", 800, 69)
			action = () => {
				GamePanel.changeView("CampaignMenu")
			}
		},
		new Button(new Point(640, 600), new Dimension(800, 160)) {
			spriteBack    = SpriteLoader.fromResource("menuButtonLarge.png")
			spriteFront   = SpriteLoader.fromString("quit & take a break", 800, 69)
			action = () => {
				TowerDefense.quitGame
			}
		}
	)

	def render(time: Double, delta: Double): Unit = {
		for(b <- buttons)
			b.render(time, delta)
	}
}