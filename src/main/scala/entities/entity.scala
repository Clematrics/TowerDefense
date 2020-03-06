import java.awt.Graphics2D;

/**
	* The Entity class is the base class for all objects present in a level, such
	* as towers and monsters.
	*/
abstract class Entity {
	var valid: Boolean = true
	def tick(time: Double, delta: Double): Unit
	def render(time: Double, delta: Double): Unit
}