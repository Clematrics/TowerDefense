import scala.swing._
import scala.swing.event._
import scala.swing.Swing._
import java.awt.{Color, Graphics2D, geom, Point}
import javax.swing.ImageIcon
import java.awt.geom.AffineTransform
import scala.io.Source

object TowerDefense extends SimpleSwingApplication {
	lazy val ui: Panel = new Panel {
		background = Color.white
		preferredSize = (300, 200)

		focusable = true
		listenTo(mouse.clicks, mouse.moves, keys)

		reactions += {
			case e: MousePressed =>
				moveTo(e.point)
				requestFocusInWindow()
			case e: MouseDragged => lineTo(e.point)
			case e: MouseReleased => lineTo(e.point)
			case KeyTyped(_, 'c', _, _) =>
				path = new geom.GeneralPath
				repaint()
			case _: FocusLost => repaint()
		}

		/* records the dragging */
		var path = new geom.GeneralPath

		val schwoon = new ImageIcon(getClass.getResource("schwoon.jpg"))

		def lineTo(p: Point): Unit = {
			path.lineTo(p.x, p.y); repaint()
		}

		def moveTo(p: Point): Unit = {
			path.moveTo(p.x, p.y); repaint()
		}

		override def paintComponent(g: Graphics2D): Unit = {
			super.paintComponent(g)
			g.setColor(new Color(100, 100, 100))
			val h = size.height
			g.drawString("Press left mouse button and drag to paint.", 10, h - 26)
			if (hasFocus) g.drawString("Press 'c' to clear.", 10, h - 10)
			g.setColor(Color.black)
			g.draw(path)
			val p = path.getCurrentPoint
			if (p != null) {
				g.translate(p.getX, p.getY)
				g.drawImage(schwoon.getImage(), new AffineTransform, null)
			}
		}
	}

	def top = new MainFrame {
		title = "Tower Def[ENS]e"
		contents = ui
	}
}