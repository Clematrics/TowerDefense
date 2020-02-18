import java.awt.Graphics2D
import scala.swing.Publisher
import scala.swing.Reactor

abstract class Level(gs: GameStatus, ps: Publisher*) extends Publisher with Reactor {
	val m_gs: GameStatus
	listenTo(ps: _*)

	def tick(running_for: Double, delta: Double)
	def render(g: Graphics2D, running_for: Double, delta: Double)
}