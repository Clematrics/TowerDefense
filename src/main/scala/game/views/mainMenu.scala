import engine.core.{GamePanel, View}
import engine.loaders.SpriteLoader
import engine.interaction.Button

//import scala.swing.event._
import java.awt.{Dimension, Point}

import scala.collection.mutable.ArrayBuffer

/**
  * This class handles the startup menu of this application.
  */
class MainMenu extends View { outer =>
	buttons ++= ArrayBuffer(
		new Button(new Point(320, 60), new Dimension(400, 80)) {
			spriteBack    = SpriteLoader.fromResource("button400x80.png")
			spriteFront   = SpriteLoader.fromString("play & prove yourself !", 400, 35)
			action = () => {
				GamePanel.changeView("CampaignMenu")
			}
		},
		new Button(new Point(320, 180), new Dimension(400, 80)) {
			spriteBack    = SpriteLoader.fromResource("button400x80.png")
			spriteFront   = SpriteLoader.fromString("play against someone", 400, 35)
			action = () => {
				GamePanel.changeView("NetworkView")
			}
		},
		new Button(new Point(320, 300), new Dimension(400, 80)) {
			spriteBack    = SpriteLoader.fromResource("button400x80.png")
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