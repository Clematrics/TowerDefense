import java.awt.geom.AffineTransform
import java.awt.{Dimension, Point}
import java.net.{InetAddress, NetworkInterface}

import engine.core.{GamePanel, Renderer, View}
import engine.interaction.Button
import engine.loaders.SpriteLoader

import scala.collection.mutable.ArrayBuffer

class NetworkView extends View {
	outer =>
	var players: ArrayBuffer[String] = ArrayBuffer()
	var myToken: String = scala.util.Random.nextInt().toString

	buttons = ArrayBuffer(
		new Button(new Point(40, 20), new Dimension(75, 30)) {
			spriteBack = SpriteLoader.fromResource("menuButtonLarge.png")
			spriteFront = SpriteLoader.fromString("go back", 75, 15)
			action = () => {
				GamePanel.changeView("MainMenu")
			}
		}
	)

	TowerDefense.connect(onReceive)
	sayHello()

	/**
	  * This method is called whenever a message is received.
	  * @param from		Address of sender
	  * @param message	Content of the message
	  */
	def onReceive(from: String, message: String): Unit = {
		val command: Array[String] = message.split(" ")
		if (command(0) == "Hello") {
			if (command.length == 2) {
				receiveHello(from, command(1))
			}
		}
	}

	/**
	  * Manages the 'Hello' message.
	  * @param from		Address of sender
	  * @param token	Unique token to make sure it is not our echo
	  * @return
	  */
	def receiveHello(from:String, token: String) = {
		if (token != myToken) {
			addPlayer(from)
			sayHello
		}
	}

	/**
	  * Send a greeting message over the network.
	  */
	def sayHello(): Unit = {
		TowerDefense.sendMessage("Hello " + myToken)
	}

	/**
	  * Adds the player who just said hello.
	  * @param addr IP of the player
	  */
	def addPlayer(addr: String): Unit = {
		players.synchronized {
			if (!players.contains(addr)) {
				val l = players.length
				val x = (l % 4) * 150 + 80
				val y = (l / 4) * 50 + 100
				buttons += new Button(new Point(x, y), new Dimension(100, 30)) {
					spriteBack = SpriteLoader.fromResource("menuButtonLarge.png")
					spriteFront = SpriteLoader.fromString(addr, 150, 15)
					action = () => {
						println("Player" + addr + " asked to participate.")
					}
				}
				players += addr
			}
		}
	}

	override def render(time: Double, delta: Double): Unit = {
		val spriteTitle = SpriteLoader.fromString("Players in the local area network", 640, 28)
		Renderer.userInterface.drawImage(spriteTitle,
			new AffineTransform(1, 0, 0, 1,
				320 - spriteTitle.getWidth(null) / 2, 0), null)

		for (b <- buttons) {
			b.render(time, delta)
		}
	}
}
