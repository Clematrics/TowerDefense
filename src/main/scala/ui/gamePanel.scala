import scala.swing.Panel
import scala.swing.Swing.pair2Dimension
import java.awt.{Color, Graphics2D, Toolkit}
import scala.swing.event.FocusLost
import scala.swing.event.KeyTyped
import java.awt.event.ComponentAdapter
import java.awt.event.ComponentEvent

/**
  * GamePanel is the main component of this application. It hosts all the objects and menus
  * used during the execution.
  */
object GamePanel extends Panel {
	background = Color.BLACK
	preferredSize = (Cst.windowWidth, Cst.windowHeight)
	focusable = true

	listenTo(keys)
	reactions += {
		case Tick(t, d) =>
			running_for = t
			delta       = d
			view.tick(t, d)
			repaint()
		case _: FocusLost => repaint()
	}

	val ps = List(mouse.moves, mouse.clicks, keys)

	var view: View = new MainMenu
	view.listenTo(ps: _*)

	var running_for = 0.0
	var delta       = 0.0
	val timer = new Timer {
		interval = 16
		run = true
	}
	listenTo(timer)

	def changeView(levelName: String) {
		view.deafTo(ps: _*)
		val constr = Class.forName(levelName).getConstructor()
		view = constr.newInstance().asInstanceOf[View]
		repaint()
		new Delay(50, () => view.listenTo(ps: _*)) { run = true }
	}

	override def paintComponent(g: Graphics2D) {
		super.paintComponent(g)

		Renderer.prepareRendering

		view.render(running_for, delta)
		// frame per second display
		if (Renderer.debugMode) {
			Renderer.debug.setColor(new Color(240, 0, 0))
			Renderer.debug.drawString(f"$delta%.1f ms", 0, 10)
		}

		Renderer.closeRendering
		Renderer.mergeLayers(g)

		// for smoother rendering, according to https://stackoverflow.com/questions/35436094/scala-swing-performance-depends-on-events
		Toolkit.getDefaultToolkit().sync()
	}

	def quit(): Unit = {
	  GamePanel.timer.run = false
		TowerDefense.quit
	}
}