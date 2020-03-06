import scala.swing.event._
import java.awt.{Color, Dimension, Graphics2D, Point}
import java.awt.geom.AffineTransform
import scala.collection.mutable.ArrayBuffer

class LoseMenu extends View { outer =>
	buttons ++= ArrayBuffer(
		new Button(new Point(640, 420), new Dimension(800, 160)) {
			sprite_back    = SpriteLoader.fromResource("menuButtonLarge.png")
			sprite_front   = SpriteLoader.fromString("Retry", 800, 69)
			action = () => {
				Game.reset
				GamePanel.changeView("DefensePhase")
			}
		}
	)

	def render(running_for: Double, delta: Double): Unit = {
		val loseImg = SpriteLoader.fromString("You lose!", 800, 69)
		Renderer.userInterface.drawImage(loseImg, new AffineTransform(1, 0, 0, 1, 640 - loseImg.getWidth(null) / 2, 220), null)

		for(b <- buttons) {
			b.render(running_for, delta)
		}
	}
}