import scala.swing.Panel
import scala.swing.Swing.pair2Dimension
import java.awt.{Color, Graphics2D, Toolkit}
import scala.swing.event.FocusLost

/**
  * GamePanel is the main component of this application. It hosts all the objects and menus
  * used during the execution.
  */
object GamePanel extends Panel {
	background = Color.black
	preferredSize = (1280, 720)
	focusable = true

	reactions += {
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
		override def action(r: Double, d: Double) = {
			running_for = r
			delta       = d
			lvl.tick(r, d)
			repaint()
		}
	}

	def changeLevel(levelName: String) {
		lvl.deafTo(ps: _*)
		val constr = Class.forName(levelName).getConstructor()
		lvl = constr.newInstance().asInstanceOf[Level]
		repaint()
		new Timer {
			interval = 50
			once = true
			run = true
			override def action(r: Double, d: Double) = {
				lvl.listenTo(ps: _*)
			}
		}
	}

	override def paintComponent(g: Graphics2D) {
		super.paintComponent(g)
		lvl.render(g, running_for, delta)

		// frame per second display
		g.setColor(new Color(240, 0, 0))
		g.drawString(f"$delta%.1f ms", 0, 10)

		// for smoother rendering, according to https://stackoverflow.com/questions/35436094/scala-swing-performance-depends-on-events
		Toolkit.getDefaultToolkit().sync()
	}
}