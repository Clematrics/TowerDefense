import scala.swing.event._
import java.awt.{Dimension, Graphics2D, Point}

class DefensePhase extends Level { outer =>
	reactions += {
		case MouseMoved(_, point, _) =>
			for(b <- buttons) b.onMoved(point)
		case MouseReleased(_, point, _, _, _) =>
			for(b <- buttons) b.onRelease(point)
	}

	val buttons : List[Button] = List(
		new Button(new Point(1200, 40), new Dimension(150, 60)) {
			listenTo(outer)
			sprite_back    = SpriteLoader.fromResource("menuButtonLarge.png")
			sprite_front   = SpriteLoader.fromString("fight !", 150, 60, 30)
			action = () => {
				GamePanel.changeLevel("AttackPhase")
			}
		},
		new Button(new Point(1200, 680), new Dimension(150, 60)) {
			listenTo(outer)
			sprite_back    = SpriteLoader.fromResource("menuButtonLarge.png")
			sprite_front   = SpriteLoader.fromString("go back", 150, 60, 30)
			action = () => {
				GamePanel.changeLevel("CampaignMenu")
			}
		}
	)


	def render(g: Graphics2D, running_for: Double, delta: Double): Unit = {
		for(b <- buttons) {
			b.render(g, running_for, delta)
		}
	}
}