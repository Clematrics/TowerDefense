import scala.swing.{MainFrame, SimpleSwingApplication}
import javax.swing.WindowConstants
/**
  * Entry point for TowerDefense application.
  */
object TowerDefense extends SimpleSwingApplication {
	def top = new MainFrame {
		title = "Tower Def[ENS]e"
		contents = GamePanel
		resizable = false
		
		peer.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE)

    override def closeOperation() = {
      peer.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE)
      GamePanel.quit
      close      
    }
 	}
}