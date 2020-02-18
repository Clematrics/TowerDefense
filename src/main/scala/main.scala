import scala.swing._

object TowerDefense extends SimpleSwingApplication {
	def top = new MainFrame {
		title = "Tower Def[ENS]e"
		contents = new GamePanel
	}
}