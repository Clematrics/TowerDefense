import java.awt.Graphics2D
import scala.swing.event._
import java.awt.geom.Point2D
import java.awt.Point
import java.awt.Dimension
import scala.swing.Publisher

class StartLevel(gs: GameStatus, ps: Publisher*) extends Level(gs, ps: _*) {
	val m_gs: GameStatus = gs

	reactions += {
		case KeyTyped(_, 'c', _, _) =>
			println("Key pressed")
			publish(new ChangeLevelEvent("yo"))
	}

	val butt = new Button {
		var sprite = SpriteLoader.fromString("schwoon.jpg")
		var position = new Point(100, 100)
		var size = new Dimension(200, 200)
		action = () => { println("hello"); publish(new ChangeLevelEvent("MEC...")) }
	}
	butt.listenTo(ps: _*)

	def tick(running_for: Double, delta: Double): Unit = {
	}
	def render(g: Graphics2D, running_for: Double, delta: Double): Unit = {
		butt.render(g, running_for, delta)
	}
}