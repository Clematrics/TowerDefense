import engine.core.GamePanel

import scala.swing.{MainFrame, SimpleSwingApplication}
import javax.swing.WindowConstants
import java.awt.event.ComponentAdapter
import java.awt.event.ComponentEvent
import java.awt.Dimension
import java.net.{InetAddress, _}
import java.io._
import java.util
import java.util.Arrays

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
	}

	/**
	  * ServerSocket object to receive and send messages
	  */
	var socket: MulticastSocket = new MulticastSocket(9999)
	var group: InetAddress = InetAddress.getByName("224.0.0.1")
	val bufSize: Int = 2048
	var running: Boolean = false

	val backgroundThread = new Thread {
		override def run: Unit = {

			var packet: DatagramPacket = null

			println("Server launched on port " + socket.getLocalPort().toString)

			while (running) {
				try {
					packet = new DatagramPacket(new Array[Byte](bufSize), bufSize)
					socket.receive(packet)
					val array: Array[Byte] = packet.getData
					if (isTDProtocol(array)) {
						val len: Int = array(3).toInt
						val ps: Array[Byte] = util.Arrays.copyOfRange(array, 4, 4 + len)
						val msg: Array[Byte] = util.Arrays.copyOfRange(array, 4 + len, array.length - (4 + len + 1))
						println(new String(ps) + " : " + new String(msg))
					}
					else {
						val hostname: String = packet.getAddress.getHostName
						println(hostname + " : " + new String(array))
					}

				} catch {
					case e: IOException => println("Ending server.")
					case e: InterruptedException => println("Ending server.")
				}
			}
		}
	}

	def connect(): Unit = {
		if (!running) {
			running = true
			socket.joinGroup(group)
			backgroundThread.start()
		}
	}

	/**
	  * Send a message to the other player
	  * @param text
	  */
	def sendMessage(name: String, text: String): Unit = {
		var packet: DatagramPacket = null
		val stream: ByteArrayOutputStream = new ByteArrayOutputStream

		// Protocol signature
		stream.write(Array[Byte](127, -128, 127), 0, 3)
		val ps = name.getBytes
		stream.write(Array[Byte](ps.length.toByte), 0, 1)
		stream.write(ps, 0, ps.length)
		val msg = text.getBytes
		stream.write(msg, 0, msg.length)
		val array = stream.toByteArray

		packet = new DatagramPacket(array, array.length, group, socket.getLocalPort)

		socket.send(packet)
	}

	/**
	  * Checks if datagram matches the game protocol
	  *
	  * @param packet
	  * @return
	  */
	def isTDProtocol(packet: Array[Byte]): Boolean = packet(0) == 127 && packet(1) == -128 && packet(2) == 127

	def quitGame() = {
		Game.save
		GamePanel.stop
		running = false
		socket.close
		quit
	}
}