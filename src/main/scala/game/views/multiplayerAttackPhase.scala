import engine.core.{Entity, GamePanel, Renderer, View}
import engine.helpers.{CellPoint, ScreenPoint, Delay}
import engine.loaders.SpriteLoader
import engine.interaction.{MouseHelper, Button}
import engine.Cst

import scala.swing.event._
import java.awt.{Color, Dimension, Graphics2D, Point}
import java.awt.geom.AffineTransform
import java.awt.BasicStroke
import scala.collection.mutable.ArrayBuffer

/**
  * This class is the phase of the game (seen as a Level class) where
  * enemies attack and towers defend the player.
  */
class MultiplayerAttackPhase extends View { outer =>
	var enemyToAdd: Enemy = new ProtoEnemy
	var opponentNoMoney = false

	TowerDefense.callback = onReceive

	def onReceive(from: String, message: String): Unit = {
		val command: Array[String] = message.split(" ")
		val otherToken = command(0).toInt
		if (otherToken != Game.token) {
			command(1) match {
				case "Enemy" =>
					val enemyName = command(2)
					val x = command(3).toDouble
					val y = command(4).toDouble
					val enemy = Class.forName(enemyName).getConstructor().newInstance().asInstanceOf[Enemy]

					if (enemyToAdd.isInstanceOf[MovingEnemy]) {
						val menemy = enemy.asInstanceOf[MovingEnemy]
						menemy.pos = new CellPoint(x, y)
						menemy.targetedCheckpoint = 1 // 0 is the enemy checkpoint

						val cp = Game.map.checkpoints(1)
						menemy.targetedPos = cp.a

						menemy.computePath()
					}

					Game.entities += enemy.asInstanceOf[Entity]

				case "NoMoney" =>
					opponentNoMoney = true
					if (opponentNoMoney && Game.multiplayerGold < 20) {
						Game.reset
						GamePanel.changeView("MultiplayerDrawMenu")
					}
				case "Dead" =>
					Game.reset
					GamePanel.changeView("MultiplayerWinMenu")
				case "Surrender" =>
					Game.reset
					GamePanel.changeView("MultiplayerWinMenu")
			}
		}
	}

	var mouseCursorPosition = new ScreenPoint(0, 0)

	reactions += {
		case MouseMoved(_, point, _) =>
			mouseCursorPosition = MouseHelper.fromMouse(point)
		case MouseReleased(_, point, _, _, _) =>
			val mousePos = mouseCursorPosition.toCellPoint
			if (	( 	mousePos.insideBox(new CellPoint( 0,  1), new CellPoint( 4,  4))
					||	mousePos.insideBox(new CellPoint( 0, 12), new CellPoint( 3, 16))
					||	mousePos.insideBox(new CellPoint( 0, 21), new CellPoint( 5, 27))
					||	mousePos.insideBox(new CellPoint(15, 28), new CellPoint(23, 30)) )
				&& enemyToAdd.gold <= Game.multiplayerGold
			) {
				if (enemyToAdd.isInstanceOf[MovingEnemy]) {
					val menemy = enemyToAdd.asInstanceOf[MovingEnemy]
					menemy.pos = mousePos
					menemy.targetedCheckpoint = 0 // 0 is the enemy checkpoint

					val cp = Game.map.checkpoints(0)
					menemy.targetedPos = cp.a

					menemy.computePath()
				}

				Game.opponentEntities += enemyToAdd.asInstanceOf[Entity]
				Game.multiplayerGold -= enemyToAdd.gold
				if (Game.multiplayerGold < 20) {
					new Delay(30000, () => TowerDefense.sendMessage(f"${Game.token} NoMoney")) { run = true }
				}
				val enemyName = enemyToAdd.getClass.getName
				val mirrored = mousePos.mirror()
				TowerDefense.sendMessage(f"${Game.token} Enemy ${enemyName} ${mirrored.x} ${mirrored.y}")
				enemyToAdd = Class.forName(enemyName).getConstructor().newInstance().asInstanceOf[Enemy]
			}
	}

	buttons ++= ArrayBuffer(
		new Button(new Point(600, 20), new Dimension(75, 30)) {
			spriteBack    = SpriteLoader.fromResource("menuButtonLarge.png")
			spriteFront   = SpriteLoader.fromString("surrender", 75, 15)
			action = () => {
				Game.reset
				TowerDefense.sendMessage(f"${Game.token} Surrender")
				GamePanel.changeView("MultiplayerLoseMenu")
			}
		},
		new Button(new Point(585, 140), new Dimension(30, 30), false) {
			spriteBack = SpriteLoader.fromResource("menuButtonLarge.png")
			spriteFront = SpriteLoader.fromResource("pion.png")
			spriteTooltip = SpriteLoader.tooltip("Proto enemy\nCost : 20 Gold\nDamage : 10\nSpeed : 1.2\nA basic chess piece")
			action = () => {
				enemyToAdd = new ProtoEnemy
			}
		},
		new Button(new Point(585, 175), new Dimension(30, 30), false) {
			listenTo(outer)
			spriteBack = SpriteLoader.fromResource("menuButtonLarge.png")
			spriteTooltip = SpriteLoader.tooltip("Sphere enemy\nCost : 50 Gold\nDamage : 5\nSpeed : 2.4\nA high dimensional sphere. Careful with it!")
			action = () => {
				enemyToAdd = new SphereEnemy
			}
		},
		new Button(new Point(585, 210), new Dimension(30, 30), false) {
			spriteBack = SpriteLoader.fromResource("menuButtonLarge.png")
			spriteFront = SpriteLoader.fromResource("anim/dragon0.png")
			spriteTooltip = SpriteLoader.tooltip("Dragon\nCost : 80 Gold\nDamage : 20\nSpeed : 6\nA dragon. He can fly above the world!")
			action = () => {
				enemyToAdd = new FlyingEnemy
			}
		},
		new Button(new Point(585, 245), new Dimension(30, 30), false) {
			spriteBack = SpriteLoader.fromResource("menuButtonLarge.png")
			spriteFront = SpriteLoader.fromResource("anim/boss0.png")
			spriteTooltip = SpriteLoader.tooltip("Fire boss\nCost : 200 Gold\nDamage : 40\nSpeed : 0.6\nA burning boss doing serious damages")
			action = () => {
				enemyToAdd = new FireBoss
			}
		}
	)

	override def tick(time: Double, delta: Double): Unit = {
		if (Game.health <= 0) {
			Game.reset
			TowerDefense.sendMessage(f"${Game.token} Dead")
			GamePanel.changeView("MultiplayerLoseMenu")
		}

		for (e <- Game.entities)
			e.tick(time, delta)
		val saveHealth = Game.health

		// swapping
		val tempEnt = Game.entities
		Game.entities = Game.opponentEntities
		Game.opponentEntities = tempEnt
		val tempMap = Game.map
		Game.map = Game.opponentMap
		Game.opponentMap = tempMap

		for (e <- Game.entities)
			e.tick(time, delta)
		Game.health = saveHealth // Here, entities attacking the enemy are reducing our own health bar. We are restoring it

		// swapping
		val tempEnt2 = Game.entities
		Game.entities = Game.opponentEntities
		Game.opponentEntities = tempEnt2
		val tempMap2 = Game.map
		Game.map = Game.opponentMap
		Game.opponentMap = tempMap2

		Game.entities = Game.entities.filter((p: Entity) => p.valid)
		Game.opponentEntities = Game.opponentEntities.filter((p: Entity) => p.valid)

		// if (wave.length == 0 && Game.entities.filter(_.isInstanceOf[Enemy]).length == 0) {
		// 	GamePanel.changeView("WinMenu")
		// }
	}

	/**
	  * Rendering of the game, with the map and all entities
	  *
	  * @param g A Graphics2D object representing the drawing surface
	  * @param time Total time the game has been running
	  * @param delta
	  */
	def render(time: Double, delta: Double): Unit = {
		if (Renderer.debugMode) {
			Renderer.background.drawImage(Game.map.mapLayout, new AffineTransform(12, 0, 0, 12, 0, 0), null)
			for(cp <- Game.map.checkpoints) {
				val stroke = new BasicStroke(2)
				Renderer.background.setStroke(stroke)
				Renderer.background.setColor(new Color(255, 0, 255, 255))
				val spa = cp.a.toScreenPosition
				val spb = cp.b.toScreenPosition
				Renderer.background.drawLine(spa.x, spa.y, spb.x, spb.y)
			}
		}
		else {
			Renderer.background.drawImage(Game.map.mapImg, new AffineTransform(3, 0, 0, 3, 0, 0), null)
		}

		for(e <- Game.entities) {
			e.render(time, delta)
		}
		for(e <- Game.opponentEntities) {
			e.render(time, delta)
		}

		for(b <- buttons) {
			b.render(time, delta)
		}

		val mousePos = mouseCursorPosition.toCellPoint
		if (mousePos.x <= Cst.mapWidth - 1 && mousePos.y <= Cst.mapHeight - 1) {
			if (	( 	mousePos.insideBox(new CellPoint( 0,  1), new CellPoint( 4,  4))
					||	mousePos.insideBox(new CellPoint( 0, 12), new CellPoint( 3, 16))
					||	mousePos.insideBox(new CellPoint( 0, 21), new CellPoint( 5, 27))
					||	mousePos.insideBox(new CellPoint(15, 28), new CellPoint(23, 30)) )
				&& enemyToAdd.gold <= Game.multiplayerGold
				&& enemyToAdd.isInstanceOf[MovingEnemy]
			) {
				enemyToAdd.asInstanceOf[MovingEnemy].pos = mousePos
				enemyToAdd.render(time, delta)
			}
			else {
				val stroke = new BasicStroke(4)
				Renderer.userInterface.setStroke(stroke)
				Renderer.userInterface.setColor(Color.RED)
				val x = mousePos.x.toInt * Cst.cellSize
				val y = mousePos.y.toInt * Cst.cellSize
				Renderer.userInterface.drawLine(x, y, x + Cst.cellSize, y + Cst.cellSize)
				Renderer.userInterface.drawLine(x + Cst.cellSize, y, x, y + Cst.cellSize)
			}
		}

		Renderer.userInterface.setColor(Color.BLACK)
		Renderer.userInterface.fillRect(610, 50, 20, 300)
		Renderer.userInterface.setColor(Color.RED)
		Renderer.userInterface.fillRect(610, 50 + ((Game.maxHealth - Game.health) * 300 / Game.maxHealth), 20, Game.health * 300 / Game.maxHealth)

		val stroke = new BasicStroke(2)
		Renderer.userInterface.setStroke(stroke)
		Renderer.userInterface.setColor(Color.WHITE)
		Renderer.userInterface.drawRect( 0 * Cst.cellSize,  1 * Cst.cellSize, ( 4 -  0) * Cst.cellSize, ( 4 -  1) * Cst.cellSize)
		Renderer.userInterface.drawRect( 0 * Cst.cellSize, 12 * Cst.cellSize, ( 3 -  0) * Cst.cellSize, (16 - 12) * Cst.cellSize)
		Renderer.userInterface.drawRect( 0 * Cst.cellSize, 21 * Cst.cellSize, ( 5 -  0) * Cst.cellSize, (27 - 21) * Cst.cellSize)
		Renderer.userInterface.drawRect(15 * Cst.cellSize, 28 * Cst.cellSize, (23 - 15) * Cst.cellSize, (30 - 28) * Cst.cellSize)
	}
}