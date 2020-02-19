import java.awt.Graphics2D;

abstract class Entity {
	def update():Unit
	def render(g:Graphics2D):Unit = {}
}