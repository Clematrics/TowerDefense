import scala.swing._
import scala.swing.Swing._
import java.awt.{Color, Graphics2D, Toolkit}
import scala.swing.event._

class GamePanel extends Panel {
	background = Color.white
	preferredSize = (1280, 720)
	focusable = true
	opaque = false  // for smoother rendering

	var gameStatus = new GameStatus
	var lvl = new StartLevel(gameStatus)

	lvl.listenTo(mouse.moves, mouse.clicks, keys)
	listenTo(keys, lvl)

	var p = false

	val timer = new Timer {
		interval = 16
		run = true
	}

	listenTo(timer)
	var x: Int = 50
	var delta  = 0.0

	reactions += {
		case cl : ChangeLevelEvent =>
			println(cl.m_s)
		case KeyTyped(_, 'p', _, _) =>
			p = true
			println("pressed p")
			repaint()
		case Tick(_, t, delta) =>
			this.delta = delta
			x = (x + (0.1 * delta).toInt) % size.width
			repaint()
		case _: FocusLost => repaint()
	}

	override def paintComponent(g: Graphics2D) {
		super.paintComponent(g)
		if (p)
			g.drawString("Paint!", x, 50)
		lvl.render(g, 0)
		g.drawString(delta.toString + "ms", 100, 100)

		// for smoother rendering
		Toolkit.getDefaultToolkit().sync();
	}
}