import scala.swing.event._
import java.awt.{Dimension, Graphics2D, Point}

/**
  * This class handles the startup menu of this application.
  */
class MainMenu extends Level { outer =>
	reactions += {
		case MouseMoved(_, point, _) =>
			for(b <- buttons) b.onMoved(point)
		case MouseReleased(_, point, _, _, _) =>
			for(b <- buttons) b.onRelease(point)
	}

	val buttons : List[Button] = List(
		new Button(new Point(640, 200), new Dimension(800, 160)) {
			sprite_back    = SpriteLoader.fromResource("menuButtonLarge.png")
			sprite_front   = SpriteLoader.fromString("play & prove yourself !", 800, 160, 69)
			action = () => {
				GamePanel.changeLevel("CampaignMenu")
			}
		},
		new Button(new Point(640, 600), new Dimension(800, 160)) {
			sprite_back    = SpriteLoader.fromResource("menuButtonLarge.png")
			sprite_front   = SpriteLoader.fromString("quit & take a break", 800, 160, 69)
			action = () => {
				TowerDefense.quit
			}
		}
	)

	def render(g: Graphics2D, running_for: Double, delta: Double): Unit = {
		for(b <- buttons)
			b.render(g, running_for, delta)
	}
}