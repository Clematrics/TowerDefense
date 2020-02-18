import java.awt.Graphics2D
import scala.swing.event._

class StartLevel(gs: GameStatus) extends Level(gs) {
	val m_gs: GameStatus = gs

	reactions += {
		case KeyTyped(_, 'c', _, _) =>
			println("Key pressed")
			publish(new ChangeLevelEvent("yo"))
	}

	def tick(time: Double): Unit = {
	}
	def render(g: Graphics2D, time: Double): Unit = {
	}
}