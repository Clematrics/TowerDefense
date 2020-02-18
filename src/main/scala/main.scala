import scala.swing._
import scala.swing.event._
import scala.swing.Swing._
import java.awt.{Color, Graphics2D, geom, Point}
import javax.swing.ImageIcon
import java.awt.geom.AffineTransform
import scala.io.Source

object TowerDefense extends SimpleSwingApplication {
	def top = new MainFrame {
		title = "Tower Def[ENS]e"
		contents = new GamePanel
	}
}