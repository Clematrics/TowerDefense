import engine.core.GamePanel

import scala.swing.{MainFrame, SimpleSwingApplication}
import javax.swing.WindowConstants
import java.awt.event.ComponentAdapter
import java.awt.event.ComponentEvent
import java.awt.Dimension
import java.net.{InetAddress, _}
import java.io._
import java.util
import java.io.DataOutputStream
import java.nio.ByteBuffer
import scala.io._

/**
  * Entry point for TowerDefense application.
  */
object TowerDefense extends SimpleSwingApplication {
	td =>
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
	}

	/**
	  * ServerSocket object to receive and send messages
	  */
	var socket: MulticastSocket = null
	var group: InetAddress = null
	val bufSize: Int = 2048
	var running: Boolean = false
	var callback: (String, String) => Unit = null

	var backgroundThread: Thread = null

	var connexionToken: String = ""

	/**
	  * Starts the network service
	  *
	  * @param fCallback The function to be called on receiving a message.
	  */
	def connect(fCallback: (String, String) => Unit, token: String): Unit = {
		if (!running) {
			socket = new MulticastSocket(9999)
			group = InetAddress.getByName("239.0.0.1")
			callback = fCallback
			running = true
			try {
				socket.joinGroup(group)
				backgroundThread = new Thread {
					override def run: Unit = {
						var packet: DatagramPacket = null

						println("Server launched on port " + socket.getLocalPort().toString)

						while (running) {
							try {
								packet = new DatagramPacket(new Array[Byte](bufSize), bufSize)
								socket.receive(packet)
								val array: Array[Byte] = packet.getData
								if (isTDProtocol(array)) {
									val len: Int = ByteBuffer.wrap(util.Arrays.copyOfRange(array, 3, 7)).getInt
									val msg: Array[Byte] = util.Arrays.copyOfRange(array, 7, math.min(array.length, 7 + len))

									callback(packet.getAddress().getHostAddress, new String(msg))
								}
							} catch {
								case e: IOException => println("Ending server.")
								case e: InterruptedException => println("Ending server.")
							}
						}
					}
				}
				backgroundThread.start()
				connexionToken = token
			} catch {
				case s: SocketException => println("Connexion error")
					running = false
			}
		}
	}

	def disconnect: Unit = {
		if (running) {
			sendMessage(s"Bye $connexionToken")
			running = false
			socket.close
		}
	}

	/**
	  * Send a message to the other player
	  *
	  * @param text
	  */
	def sendMessage(text: String): Unit = {
		var packet: DatagramPacket = null
		val stream: ByteArrayOutputStream = new ByteArrayOutputStream

		val dos = new DataOutputStream(stream)
		// Protocol signature
		stream.write(Array[Byte](127, -128, 127), 0, 3)
		dos.writeInt(text.length)
		val msg = text.getBytes
		stream.write(msg, 0, msg.length)
		val array = stream.toByteArray

		packet = new DatagramPacket(array, array.length, group, socket.getLocalPort)

		socket.send(packet)
	}

	/**
	  * Checks if the packet matches the game protocol
	  * The TowerDef[ENS]e protocol :
	  * Offset				 0  1  2    3  4  5  6    7 ... (7+L-1)
	  * Data	Magic number 7F 80 7F | text length L | text
	  *
	  * @param packet
	  * @return
	  */
	def isTDProtocol(packet: Array[Byte]): Boolean = packet(0) == 127 && packet(1) == -128 && packet(2) == 127

	def quitGame() = {
		Game.save
		GamePanel.stop
		disconnect
		quit
	}
}