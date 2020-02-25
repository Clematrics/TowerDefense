import java.awt.Graphics2D;

/**
	* The Entity class is the base class for all objects present in a level, such
	* as towers and monsters.
	*/
abstract class Entity {
	var valid: Boolean = true
	def tick(running_for: Double, delta: Double): Unit
	def render(g: Graphics2D): Unit = {}
}