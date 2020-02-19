import java.awt.Graphics2D
import scala.swing.Publisher

abstract class Level extends Publisher {
	def tick(running_for: Double, delta: Double) {}
	def render(g: Graphics2D, running_for: Double, delta: Double)
}