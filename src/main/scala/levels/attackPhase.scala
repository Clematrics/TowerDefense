import scala.swing.event._
import java.awt.{Dimension, Graphics2D, Point}

class AttackPhase extends Level { outer =>
	reactions += {
		case MouseMoved(_, point, _) =>
			for(b <- buttons) b.onMoved(point)
		case MouseReleased(_, point, _, _, _) =>
			for(b <- buttons) b.onRelease(point)
	}

	val buttons : List[Button] = List(
	)


	def render(g: Graphics2D, running_for: Double, delta: Double): Unit = {
		for(b <- buttons) {
			b.render(g, running_for, delta)
		}
	}
}