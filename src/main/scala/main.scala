import engine.core.GamePanel

import scala.swing.{MainFrame, SimpleSwingApplication}
import javax.swing.WindowConstants
import java.awt.event.ComponentAdapter
import java.awt.event.ComponentEvent
import java.awt.Dimension
import java.net._
import java.io._
import scala.io._

/**
  * Entry point for TowerDefense application.
  */
object TowerDefense extends SimpleSwingApplication { td =>
	def top = new MainFrame {
		title = "Tower Def[ENS]e"
		contents = GamePanel
		GamePanel.changeView("MainMenu")
		centerOnScreen
		Game.load()

		//peer.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE) Si jamais on voulait demander la confirmation

		override def closeOperation() = {
			//peer.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE)
			td.quitGame
		}

		backgroundThread.start()
	}


	/**
	  * ServerSocket object to receive messages
	  */
	var server: ServerSocket = new ServerSocket(9999)

	val backgroundThread = new Thread {
		override def run: Unit = {
			println("Server launched on port " + server.getLocalPort().toString)
			try {
				while (true) {
					val s = server.accept()
					val in = new BufferedSource(s.getInputStream()).getLines()
					val out = new PrintStream(s.getOutputStream())

					val message = in.next()
					println(s.getInetAddress().getHostAddress + message)
					//out.flush()									<- Echo to client
					s.close()
				}
			} catch {
				case e: InterruptedException => println("Ending server.")
				case e: SocketException =>  println("Ending server.")
			}

		}
	}

	def sendMessage(text: String): Unit = {
		/**
		  * Socket object to send messages (to localhost for now)
		  */
		val client = new Socket(InetAddress.getByAddress(Array(127,0,0,1)), 9999)
		//lazy val in = new BufferedSource(s.getInputStream()).getLines()	<- Echo
		val out = new PrintStream(client.getOutputStream())

		out.println(text)
		out.flush()
		//println("Received: " + in.next())									<- Echo from server
	}


	def quitGame() = {
		Game.save()
		GamePanel.stop
		server.close()
		quit
	}
}