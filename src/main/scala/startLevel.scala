import scala.swing.event._
import java.awt.{Dimension, Graphics2D, Point}

class StartLevel extends Level { outer =>
	reactions += {
		case MouseMoved(_, point, _) =>
			for(b <- buttons) b.onMoved(point)
		case MouseReleased(_, point, _, _, _) =>
			for(b <- buttons) b.onRelease(point)
		case KeyTyped(_, 'c', _, _) =>
			println("Key pressed from start")
	}

	val buttons : List[Button] = List(
		new Button(new Point(640, 200), new Dimension(800, 160)) {
			listenTo(outer)
			sprite_back    = SpriteLoader.fromResource("menuButtonLarge.png")
			sprite_front   = SpriteLoader.fromString("play !", 800, 160, 100)
			sprite_tooltip = SpriteLoader.tooltip("A long text that can't stand in a short 200 pixel wide line")
			action = () => {
				println("hello from start")
				GamePanel.changeLevel("TestLevel")
			}
		}
	)

	def render(g: Graphics2D, running_for: Double, delta: Double): Unit = {
		for(b <- buttons)
			b.render(g, running_for, delta)
	}
}