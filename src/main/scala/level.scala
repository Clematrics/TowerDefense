import java.awt.Graphics2D
import scala.swing.Publisher

abstract class Level(gs: GameStatus) extends Publisher {
	val m_gs: GameStatus

	def tick(running_for: Double, delta: Double) {}
	def render(g: Graphics2D, running_for: Double, delta: Double)
}