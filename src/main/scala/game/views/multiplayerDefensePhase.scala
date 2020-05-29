import engine.core.{GamePanel, Renderer, View}
import engine.helpers.{ScreenPoint}
import engine.loaders.SpriteLoader
import engine.interaction.{MouseHelper, Button}
import engine.map.{Path, Wall, EmptyTowerCell, OccupiedTowerCell}
import engine.core.Entity

import scala.swing.event._
import java.awt.{Color, Dimension, Graphics2D, Point}
import java.awt.geom.AffineTransform
import java.awt.BasicStroke
import scala.collection.mutable.ArrayBuffer
import engine.Cst
import scala.swing.Reactions
import engine.helpers.CellPoint

/**
  * This class is the phase of the game where the player can buy
  * and add towers to defend the place.
  */
class MultiplayerDefensePhase extends View { outer =>
	var ready = false
	var opponentReady = false
	TowerDefense.callback = onReceive

	def onReceive(from: String, message: String): Unit = {
		val command: Array[String] = message.split(" ")
		val otherToken = command(0).toInt
		if (otherToken != Game.token) {
			command(1) match {
				case "Tower" =>
					val towerName = command(2)
					val x = command(3).toDouble
					val y = command(4).toDouble
					val tower = Class.forName(towerName).getConstructor().newInstance().asInstanceOf[Tower]

					tower.pos = new CellPoint(x, y)
					Game.opponentEntities += tower
					if (tower.isInstanceOf[WallTower])
					Game.map.map(x.toInt)(y.toInt) = Wall
					else
					Game.map.map(x.toInt)(y.toInt) = OccupiedTowerCell

				case "End" =>
					opponentReady = true
					moveNextPhaseIfReady()
			}
		}
	}

	def moveNextPhaseIfReady(): Unit = {
		if (opponentReady && ready) {
			GamePanel.changeView("MultiplayerAttackPhase")
		}
	}

	var mouseCursorPosition = new ScreenPoint(0, 0)
	val mouseMovedReaction : Reactions.Reaction = {
		case MouseMoved(_, point, _) =>
			mouseCursorPosition = MouseHelper.fromMouse(point)
	}
	val mouseReleasedReaction : Reactions.Reaction = {
		case MouseReleased(_, point, _, _, _) =>
			val mousePos = mouseCursorPosition.toCellPoint
			if (mousePos.x <= (Cst.mapWidth / 2) - 1 && mousePos.y <= Cst.mapHeight - 1) {
				if (((Game.opponentMap.map(mousePos.x.toInt)(mousePos.y.toInt) == EmptyTowerCell && !towerToAdd.isInstanceOf[WallTower])
					|| (Game.opponentMap.map(mousePos.x.toInt)(mousePos.y.toInt) == Path && towerToAdd.isInstanceOf[WallTower])
					) && Game.multiplayerGold >= towerToAdd.cost) {
					towerToAdd.pos = mousePos
					Game.entities += towerToAdd
					val mirrored = mousePos.mirror()
					TowerDefense.sendMessage(f"${Game.token} Tower ${towerToAdd.getClass().getName()} ${mirrored.x} ${mirrored.y}")
					if (towerToAdd.isInstanceOf[WallTower])
						Game.opponentMap.map(mousePos.x.toInt)(mousePos.y.toInt) = Wall
					else
						Game.opponentMap.map(mousePos.x.toInt)(mousePos.y.toInt) = OccupiedTowerCell
					Game.multiplayerGold -= towerToAdd.cost
					val towerName = towerToAdd.getClass.getName
					towerToAdd = Class.forName(towerName).getConstructor().newInstance().asInstanceOf[Tower]
				}
			}
	}

	reactions += mouseMovedReaction
	reactions += mouseReleasedReaction

	var towerToAdd: Tower = new ArmedTower
	var compoundsBuffer: ArrayBuffer[TowerCompound] = ArrayBuffer()

	buttons ++= ArrayBuffer(
		new Button(new Point(605, 20), new Dimension(60, 30)) {
			spriteBack = SpriteLoader.fromResource("menuButtonLarge.png")
			spriteFront = SpriteLoader.fromString("Fight !", 60, 15)
			action = () => {
				ready = true
				buttons.clear()
				reactions -= mouseMovedReaction
				reactions -= mouseReleasedReaction
				TowerDefense.sendMessage(f"${Game.token} End")
				moveNextPhaseIfReady()
			}
		},
		new Button(new Point(585, 140), new Dimension(30, 30), false) {
			spriteBack = SpriteLoader.fromResource("menuButtonLarge.png")
			spriteFront = SpriteLoader.fromResource("armedtour.png")
			spriteTooltip = SpriteLoader.tooltip("Blaster Tower\nCost : 10 Gold\nRadius : 6\nReload time : 1s\nPower : 5\nA tower that shoots balls")
			action = () => {
				towerToAdd = new ArmedTower
			}
		},
		new Button(new Point(585, 175), new Dimension(30, 30), false) {
			listenTo(outer)
			spriteBack = SpriteLoader.fromResource("menuButtonLarge.png")
			spriteFront = SpriteLoader.fromResource("lasertour.png")
			spriteTooltip = SpriteLoader.tooltip("Laser Tower\nCost : 20 Gold\nRadius : 6\nReload time : 2s\nPower : 20\nLAAASEERSS!")
			action = () => {
				towerToAdd = new LaserTower
			}
		},
		new Button(new Point(585, 210), new Dimension(30, 30), false) {
			spriteBack = SpriteLoader.fromResource("menuButtonLarge.png")
			spriteFront = SpriteLoader.fromResource("tour.png")
			spriteTooltip = SpriteLoader.tooltip("Chess Tower\nCost : 15 Gold\nA noob tower that does nothing else than watching its enemies in the eyes")
			action = () => {
				towerToAdd = new ProtoTower
			}
		},
		new Button(new Point(585, 245), new Dimension(30, 30), false) {
			spriteBack = SpriteLoader.fromResource("menuButtonLarge.png")
			spriteFront = SpriteLoader.fromResource("multitour.png")
			spriteTooltip = SpriteLoader.tooltip("Multi Tower\nCost : 40 Gold\nRadius : 7\nReload time : 2s\nPower : 20\nCan shoot multiple enemies at the same time")
			action = () => {
				towerToAdd = new MultiTower
			}
		},
		new Button(new Point(620, 245), new Dimension(30, 30), false) {
			spriteBack = SpriteLoader.fromResource("menuButtonLarge.png")
			spriteFront = SpriteLoader.fromResource("wall.png")
			spriteTooltip = SpriteLoader.tooltip("Wall Tower\nCost : 50 Gold\nA destructable wall that will block enemies")
			action = () => {
				towerToAdd = new WallTower
			}
		},
		new Button(new Point(620, 280), new Dimension(30, 30), false) {
			spriteBack = SpriteLoader.fromResource("menuButtonLarge.png")
			spriteFront = SpriteLoader.fromResource("thundertour.png")
			spriteTooltip = SpriteLoader.tooltip("Thunder Tower\nCost : 500 Gold\nFreezes enemies around during 5 seconds")
			action = () => {
				towerToAdd = new ThunderTower
			}
		}
	)

	def render(time: Double, delta: Double): Unit = {
		Renderer.background.drawImage(Game.map.mapImg, new AffineTransform(3, 0, 0, 3, 0, 0), null)

		if (Renderer.debugMode) {
			Renderer.background.drawImage(Game.map.mapLayout, new AffineTransform(12, 0, 0, 12, 0, 0), null)
			for (cp <- Game.map.checkpoints) {
				val stroke = new BasicStroke(1)
				Renderer.background.setStroke(stroke)
				Renderer.background.setColor(new Color(255, 0, 255, 255))
				val spa = cp.a.toScreenPosition
				val spb = cp.b.toScreenPosition
				Renderer.background.drawLine(spa.x, spa.y, spb.x, spb.y)
			}
		}

		for (t <- Game.entities)
			t.render(time, delta)

		/* Buffered towers */
		for (c <- compoundsBuffer)
			c.render(time, delta)

		if (!ready) {
			val mousePos = mouseCursorPosition.toCellPoint
			if (mousePos.x <= Cst.mapWidth - 1 && mousePos.y <= Cst.mapHeight - 1) {
				if (mousePos.x <= (Cst.mapWidth / 2) - 1
				&& ( 	(Game.opponentMap.map(mousePos.x.toInt)(mousePos.y.toInt) == EmptyTowerCell && !towerToAdd.isInstanceOf[WallTower])
					||	(Game.opponentMap.map(mousePos.x.toInt)(mousePos.y.toInt) == Path && towerToAdd.isInstanceOf[WallTower]))
				&& Game.multiplayerGold >= towerToAdd.cost
				&& (!towerToAdd.isInstanceOf[TowerCompound] || towerToAdd.asInstanceOf[TowerCompound].isValidDistance(compoundsBuffer, mousePos))) {
					towerToAdd.pos = mousePos
					towerToAdd.render(time, delta)
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

		} else {
			val waitImg = SpriteLoader.fromString("Waiting for opponent...", 400, 35)
			Renderer.drawOnTextLayerCentered(waitImg, 320, 110)
		}

		val gold = SpriteLoader.fromString(f"Gold : ${Game.multiplayerGold}", 60, 15)
		Renderer.drawOnTextLayer(gold,  560, 45)

		for (b <- buttons) {
			b.render(time, delta)
		}
	}

}