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
		new Button(new Point(320, 100), new Dimension(400, 80)) {
			spriteBack    = SpriteLoader.fromResource("menuButtonLarge.png")
			spriteFront   = SpriteLoader.fromString("play & prove yourself !", 400, 35)
			action = () => {
				GamePanel.changeView("CampaignMenu")
			}
		},
		new Button(new Point(320, 300), new Dimension(400, 80)) {
			spriteBack    = SpriteLoader.fromResource("menuButtonLarge.png")
			spriteFront   = SpriteLoader.fromString("quit & take a break", 400, 35)
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