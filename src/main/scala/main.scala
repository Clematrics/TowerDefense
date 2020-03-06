import scala.swing.{MainFrame, SimpleSwingApplication}
import javax.swing.WindowConstants
import java.awt.event.ComponentAdapter
import java.awt.event.ComponentEvent
import java.awt.Dimension

/**
  * Entry point for TowerDefense application.
  */
object TowerDefense extends SimpleSwingApplication {
	def top = new MainFrame { frame =>
		title = "Tower Def[ENS]e"
		contents = GamePanel
		centerOnScreen

		//peer.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE) Si jamais on voulait demander la confirmation

		override def closeOperation() = {
			//peer.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE)
			GamePanel.quit
			//close
		}
	}
}