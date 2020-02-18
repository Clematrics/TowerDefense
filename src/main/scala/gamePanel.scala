import scala.swing._
import scala.swing.Swing._
import java.awt.{Color, Graphics2D, Toolkit}
import scala.swing.event._

class GamePanel extends Panel {
	background = Color.white
	preferredSize = (1280, 720)
	focusable = true
	opaque = false  // for smoother rendering

	val timer = new Timer {
		interval = 16
		run = true
	}

	var gameStatus = new GameStatus
	var lvl = new StartLevel(gameStatus, mouse.moves, mouse.clicks, keys)

	listenTo(timer, keys, lvl)

	var running_for = 0.0
	var delta       = 0.0

	reactions += {
		case cl : ChangeLevelEvent =>
			println(cl.m_s)
			// TODO : change level
		case Tick(_, t, d) =>
			running_for = t
			delta       = d
			lvl.tick(t, d)
			repaint()
		case _: FocusLost => repaint()
	}

	override def paintComponent(g: Graphics2D) {
		super.paintComponent(g)
		lvl.render(g, running_for, delta)

		// frame per second display
		g.setColor(new Color(240, 0, 0))
		g.drawString(f"$delta%.1f ms", 0, 10)
		// for smoother rendering
		Toolkit.getDefaultToolkit().sync();
	}
}