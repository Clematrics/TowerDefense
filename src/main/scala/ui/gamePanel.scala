import scala.swing.Panel
import scala.swing.Swing.pair2Dimension
import java.awt.{Color, Graphics2D, Toolkit}
import scala.swing.event.FocusLost
import scala.swing.event.KeyTyped

/**
  * GamePanel is the main component of this application. It hosts all the objects and menus
  * used during the execution.
  */
object GamePanel extends Panel {
	background = Color.black
	preferredSize = (1280, 720)
	focusable = true
	var displayFramerate = false

	listenTo(keys)
	reactions += {
		case Tick(t, d) =>
			running_for = t
			delta       = d
			lvl.tick(t, d)
			repaint()
		case KeyTyped(_, 'f', _, _) =>
			displayFramerate = !displayFramerate
		case _: FocusLost => repaint()
	}

	val ps = List(mouse.moves, mouse.clicks, keys)

	var lvl: Level = new MainMenu
	lvl.listenTo(ps: _*)

	var running_for = 0.0
	var delta       = 0.0
	val timer = new Timer {
		interval = 16
		run = true
	}
	listenTo(timer)

	def changeLevel(levelName: String) {
		lvl.deafTo(ps: _*)
		val constr = Class.forName(levelName).getConstructor()
		lvl = constr.newInstance().asInstanceOf[Level]
		repaint()
		new Delay(50, () => lvl.listenTo(ps: _*)) { run = true }
	}

	override def paintComponent(g: Graphics2D) {
		super.paintComponent(g)
		lvl.render(g, running_for, delta)

		// frame per second display
		if (displayFramerate) {
			g.setColor(new Color(240, 0, 0))
			g.drawString(f"$delta%.1f ms", 0, 10)
		}

		// for smoother rendering, according to https://stackoverflow.com/questions/35436094/scala-swing-performance-depends-on-events
		Toolkit.getDefaultToolkit().sync()
	}
}