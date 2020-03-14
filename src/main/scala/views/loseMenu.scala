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
		new Button(new Point(640, 420), new Dimension(800, 160)) {
			spriteBack    = SpriteLoader.fromResource("menuButtonLarge.png")
			spriteFront   = SpriteLoader.fromString("Retry", 800, 69)
			action = () => {
				Game.reset
				GamePanel.changeView("DefensePhase")
			}
		}
	)

	def render(time: Double, delta: Double): Unit = {
		val loseImg = SpriteLoader.fromString("You lose!", 800, 69)
		Renderer.userInterface.drawImage(loseImg, new AffineTransform(1, 0, 0, 1, 640 - loseImg.getWidth(null) / 2, 220), null)

		for(b <- buttons) {
			b.render(time, delta)
		}
	}
}