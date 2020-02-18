import scala.swing.Panel
import scala.swing.Swing.pair2Dimension
import java.awt.{Color, Graphics2D, Toolkit}
import scala.swing.event.FocusLost

object GamePanel extends Panel {
	background = Color.black
	preferredSize = (1280, 720)
	focusable = true

	var running_for = 0.0
	var delta       = 0.0
	val timer = new Timer {
		interval = 16
		run = true
	}

	val ps = List(mouse.moves, mouse.clicks, keys)
	listenTo(timer)
	reactions += {
		case Tick(_, t, d) =>
			running_for = t
			delta       = d
			lvl.tick(t, d)
			repaint()
		case _: FocusLost => repaint()
	}

	var gameStatus = new GameStatus
	var lvl: Level = new StartLevel(gameStatus)
	lvl.listenTo(ps: _*)

	def changeLevel(levelName: String) {
		lvl.deafTo(ps: _*)
		val constr = Class.forName(levelName).getConstructor(classOf[GameStatus])
		lvl = constr.newInstance(gameStatus).asInstanceOf[Level]
		repaint()
		val t = new javax.swing.Timer(50, new java.awt.event.ActionListener {
			def actionPerformed(x: java.awt.event.ActionEvent): Unit = {
				lvl.listenTo(ps: _*)
			}
		})
		t.setRepeats(false)
		t.start()
	}

	override def paintComponent(g: Graphics2D) {
		super.paintComponent(g)
		lvl.render(g, running_for, delta)

		// frame per second display
		g.setColor(new Color(240, 0, 0))
		g.drawString(f"$delta%.1f ms", 0, 10)

		// for smoother rendering, according to https://stackoverflow.com/questions/35436094/scala-swing-performance-depends-on-events
		Toolkit.getDefaultToolkit().sync();
	}
}