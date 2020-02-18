import scala.swing.{MainFrame, SimpleSwingApplication}

object TowerDefense extends SimpleSwingApplication {
	def top = new MainFrame {
		title = "Tower Def[ENS]e"
		contents = GamePanel
		resizable = false
	}
}