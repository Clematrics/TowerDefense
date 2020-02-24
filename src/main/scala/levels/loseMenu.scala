import scala.swing.event._
import java.awt.{Color, Dimension, Graphics2D, Point}
import java.awt.geom.AffineTransform

class LoseMenu extends Level { outer =>
	reactions += {
		case MouseMoved(_, point, _) =>
			for(b <- buttons) b.onMoved(point)
		case MouseReleased(_, point, _, _, _) =>
			for(b <- buttons) b.onRelease(point)
	}

	val buttons : List[Button] = List(
		new Button(new Point(640, 420), new Dimension(800, 160)) {
			sprite_back    = SpriteLoader.fromResource("menuButtonLarge.png")
			sprite_front   = SpriteLoader.fromString("Retry", 800, 160, 69)
			action = () => {
				GamePanel.changeLevel("DefensePhase")
			}
		}
	)

	def render(g: Graphics2D, running_for: Double, delta: Double): Unit = {
		val loseImg = SpriteLoader.fromString("You lose!", 800, 160, 69)
		g.drawImage(loseImg, new AffineTransform(1, 0, 0, 1, 640, 300), null)

		for(b <- buttons) {
			b.render(g, running_for, delta)
		}
	}
}