import scala.swing._
import scala.swing.Swing._
import java.awt.{Color, Graphics2D, Toolkit}
import scala.swing.event._

class GamePanel extends Panel {
	background = Color.white
	preferredSize = (1280, 720)
	focusable = true
	opaque = false

	var gameStatus = new GameStatus
	var last_time = System.nanoTime
	var curr_time = System.nanoTime
	var lvl = new StartLevel(gameStatus)

	lvl.listenTo(mouse.clicks, keys)
	listenTo(keys, lvl)

	var p = false

	val timer = new Timer {
		interval = 16
		run = true
	}

	listenTo(timer)
	var x = 50

	reactions += {
		case cl : ChangeLevelEvent =>
			println(cl.m_s)
		case KeyTyped(_, 'p', _, _) =>
			p = true
			println("pressed p")
			repaint()
		case Tick(t) =>
			x = (x + t.interval) % size.width
			repaint()
			last_time = curr_time
			curr_time = System.nanoTime
			println("Time delta " + ((curr_time - last_time) / 1000000.0).toString)
		case _: FocusLost => repaint()
	}

	override def paintComponent(g: Graphics2D) {
		super.paintComponent(g)
		if (p)
			g.drawString("Paint!", x, 50)
		lvl.render(g, 0)
		g.drawString(((curr_time - last_time) / 1000000.0).toString + "ms", 100, 100)
		Toolkit.getDefaultToolkit().sync();
	}
}