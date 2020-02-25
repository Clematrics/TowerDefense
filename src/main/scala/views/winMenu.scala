import scala.swing.event._
import java.awt.{Color, Dimension, Graphics2D, Point}
import java.awt.geom.AffineTransform
import scala.collection.mutable._

class WinMenu extends View { outer =>
	reactions += {
		case MouseMoved(_, point, _) =>
			for(b <- buttons) b.onMoved(point)
		case MouseReleased(_, point, _, _, _) =>
			for(b <- buttons) b.onRelease(point)
	}

	val buttons : List[Button] = List(
		new Button(new Point(640, 420), new Dimension(800, 160)) {
			sprite_back    = SpriteLoader.fromResource("menuButtonLarge.png")
			sprite_front   = SpriteLoader.fromString("Explode more monsters", 800, 69)
			action = () => {
				Game.reset
				GamePanel.changeView("CampaignMenu")
			}
		}
	)

	def render(g: Graphics2D, running_for: Double, delta: Double): Unit = {
		val winImg = SpriteLoader.fromString("You win!", 800, 69)
		g.drawImage(winImg, new AffineTransform(1, 0, 0, 1, 640 - winImg.getWidth(null) / 2, 220), null)

		for(b <- buttons) {
			b.render(g, running_for, delta)
		}
	}
}