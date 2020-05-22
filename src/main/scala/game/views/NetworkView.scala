import java.awt.geom.AffineTransform
import java.awt.{Dimension, Point}
import java.net.{InetAddress, NetworkInterface}

import engine.core.{GamePanel, Renderer, View}
import engine.interaction.Button
import engine.loaders.SpriteLoader

import scala.collection.mutable.ArrayBuffer

class NetworkView extends View { outer =>
	TowerDefense.connect
	var pseudo: String = "Player"

	buttons = ArrayBuffer(
		new Button(new Point(40, 20), new Dimension(75, 30)) {
			spriteBack    = SpriteLoader.fromResource("menuButtonLarge.png")
			spriteFront   = SpriteLoader.fromString("go back", 75, 15)
			action = () => {
				GamePanel.changeView("MainMenu")
			}
		}
	)

	TowerDefense.sendMessage(pseudo, "Test on multicast")

	override def render(time: Double, delta: Double): Unit = {
		for(b <- buttons) {
			b.render(time, delta)
		}
	}
}
