import scala.swing.event._
import java.awt.{Dimension, Graphics2D, Point}

class TestLevel extends Level { outer =>

	reactions += {
		case MouseMoved(_, point, _) =>
			for(b <- buttons) b.onMoved(point)
		case MouseReleased(_, point, _, _, _) =>
			for(b <- buttons) b.onRelease(point)
		case KeyTyped(_, 'c', _, _) =>
			println("Key pressed from other level")
	}

	val buttons : List[Button] = List(
		new Button(new Point(640, 200), new Dimension(800, 160)) {
			listenTo(outer)
			sprite_back  = SpriteLoader.fromResource("menuButtonLarge.png")
			sprite_front = SpriteLoader.fromString("technically...", 800, 160, 100)
			action = () => {
				println("hello from other")
				GamePanel.changeLevel("StartLevel")
			}
		}
	)

	def render(g: Graphics2D, running_for: Double, delta: Double): Unit = {
		for(b <- buttons)
			b.render(g, running_for, delta)
	}
}