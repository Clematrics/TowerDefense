import java.awt.geom.AffineTransform
import java.awt.{Dimension, Point}
import java.net.{InetAddress, NetworkInterface}

import engine.core.GamePanel.{ps, view}
import engine.core.{GamePanel, Renderer, View}
import engine.helpers.Delay
import engine.interaction.Button
import engine.loaders.SpriteLoader

import scala.collection.mutable.ArrayBuffer

class NetworkView extends View {
	outer =>
	/**
	  * This array contains the tokens of the other players on the network
	  */
	var players: ArrayBuffer[String] = ArrayBuffer()
	/**
	  * The tokens are randomly generated integers stored as strings
	  */
	Game.token = scala.util.Random.nextInt()
	val myToken = Game.token.toString()

	/**
	  * The longest the list have been, to know where to put the next button (naive, but sufficient).
	  */
	var lastAdded: Int = 0

	TowerDefense.connect(onReceive, myToken)
	sendHello()

	buttons = ArrayBuffer(
		new Button(new Point(40, 20), new Dimension(75, 30)) {
			spriteBack = SpriteLoader.fromResource("menuButtonLarge.png")
			spriteFront = SpriteLoader.fromString("go back", 75, 15)
			spriteTooltip = SpriteLoader.tooltip("Disconnect from the network")
			action = () => {
				TowerDefense.disconnect
				GamePanel.changeView("MainMenu")
			}
		}
	)

	def getArgsNumber(command: String): Int = {
		command match {
			case "Hello" => 2
			case "Fight" => 3
			case "Bye" => 2
			case "Confirm" => 3
		}
	}

	/**
	  * This method is called whenever a message is received.
	  *
	  * @param from    Address of sender
	  * @param message Content of the message
	  */
	def onReceive(from: String, message: String): Unit = {
		val command: Array[String] = message.split(" ")
		if (getArgsNumber(command(0)) == command.length) {
			command(0) match {
				case "Hello" =>
					receiveHello(from, command(1))
				case "Fight" =>
					receiveProposition(command(1), command(2))
				case "Confirm" =>
					receiveConfirmation(command(1), command(2))
				case "Bye" =>
					receiveBye(command(1))
			}
		}
	}

	/**
	  * Manages the 'Hello' message.
	  *
	  * @param from  Address of sender
	  * @param token Unique token to make sure it is not our echo
	  */
	def receiveHello(from: String, token: String): Unit = {
		if (token != myToken) {
			addPlayer(from, token)
		}
	}

	/**
	  * Send a greeting message over the network.
	  */
	def sendHello(): Unit = {
		TowerDefense.sendMessage(s"Hello $myToken")
	}

	/**
	  * In case of a 'Confirm' message
	  * Syntax : Confirm (from) (to)
	  * This message can only be received if we sent a 'Fight' message earlier.
	  *
	  * @param from The token of the player who wants to play
	  * @param to   The player he wants to play with
	  */
	def receiveConfirmation(from: String, to: String) = {
		if (to == myToken) {
			println(s"Going to play with $from, because he just confirmed")
			Game.opponentToken = from.toInt
			Game.map = MapLoader.loadMap("multiplayer")
			Game.opponentMap = MapLoader.loadMap("multiplayer")
			GamePanel.changeView("MultiplayerDefensePhase")
		}
	}

	/**
	  * Confirms the player wants to play with (from)
	  * Syntax : Confirm (from) (to)
	  *
	  * @param from
	  */
	def sendConfirmation(from: String): Unit = {
		TowerDefense.sendMessage(s"Confirm $myToken $from")
	}

	/**
	  * In case of a 'Fight' message
	  * Syntax : Fight (from) (to)
	  *
	  * @param from The token of the player who wants to play
	  * @param to   The player he wants to play with
	  */
	def receiveProposition(from: String, to: String) = {
		if (to == myToken) {
			val idx = players.indexOf(from)
			if (idx >= 0) {
				buttons(idx + 1).spriteBack = SpriteLoader.fromResource("menuButtonLargeG.png")
				buttons(idx + 1).spriteTooltip = SpriteLoader.tooltip("Fight now")
				buttons(idx + 1).action = () => {
					sendConfirmation(from)
					println(s"I agreed to play with the player $from !")
					Game.opponentToken = from.toInt
					Game.map = MapLoader.loadMap("multiplayer")
					Game.opponentMap = MapLoader.loadMap("multiplayer")
					GamePanel.changeView("MultiplayerDefensePhase")
				}
			}
		}
	}

	/**
	  * 'Fight' message
	  * Syntax : Fight (from) (to)
	  *
	  * @param token
	  */
	def sendProposition(token: String) = {
		TowerDefense.sendMessage(s"Fight $myToken $token")
	}

	/**
	  * On reception of a 'Bye' message.
	  *
	  * @param from A player who stopped the game.
	  */
	def receiveBye(from: String): Unit = {
		if (from != myToken) {
			val idx = players.indexOf(from)
			if (idx >= 0) {
				players.remove(idx)
				buttons.remove(1 + idx)
			}
		}
	}

	/**
	  * Adds the player who just said hello.
	  *
	  * @param addr IP of the player
	  */
	def addPlayer(addr: String, token: String): Unit = {
		players.synchronized {
			if (!players.contains(token)) {
				val l = lastAdded
				val x = (l % 3) * 200 + 80
				val y = (l / 3) * 50 + 100
				buttons += new Button(new Point(x, y), new Dimension(200, 30)) {
					spriteBack = SpriteLoader.fromResource("menuButtonLarge.png")
					spriteFront = SpriteLoader.fromString(s"Player #${shortenToken(token)}", 200, 15)
					spriteTooltip = SpriteLoader.tooltip(s"From $addr\nSend an invitation")
					action = () => {
						sendProposition(token)
					}
				}
				players += token
				lastAdded = players.length max lastAdded
				new Delay(1000, () => sendHello) { run = true }
			}
		}
	}

	val spriteTitle = SpriteLoader.fromString("Players in the local area network", 640, 28)
	val spriteToken = SpriteLoader.fromString(s"You are player ${shortenToken(myToken)}", 150, 15)

	def shortenToken(token: String): String = {
		if (token.length < 4) {
			token
		} else {
			token.substring(0, 2) + token.substring(token.length - 2, token.length)
		}
	}

	override def render(time: Double, delta: Double): Unit = {
		Renderer.drawOnTextLayerCentered(spriteTitle, 320, 0)
		Renderer.drawOnTextLayerCentered(spriteToken, 320, 25)

		for (b <- buttons) {
			b.render(time, delta)
		}
	}
}
