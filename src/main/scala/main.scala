import scala.swing.{MainFrame, SimpleSwingApplication}

/**
  * Entry point for TowerDefense application.
  */
object TowerDefense extends SimpleSwingApplication {
	def top = new MainFrame {
		title = "Tower Def[ENS]e"
		contents = GamePanel
		resizable = false
	}
}