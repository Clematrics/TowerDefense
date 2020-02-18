import java.awt.Graphics2D
import scala.swing.Publisher
import scala.swing.Reactor

abstract class Level(gs: GameStatus) extends Publisher with Reactor {
	val m_gs: GameStatus
	// var publisher: Publisher
	// var reactions: Reactor

	def tick(time: Double)
	def render(g: Graphics2D, time: Double)
}