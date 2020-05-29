import engine.core.{GamePanel, Renderer, View}
import engine.helpers.{ScreenPoint}
import engine.loaders.SpriteLoader
import engine.interaction.{MouseHelper, Button}
import engine.map.{Path, Wall, EmptyTowerCell, OccupiedTowerCell}

import scala.swing.event._
import java.awt.{Color, Dimension, Graphics2D, Point}
import java.awt.geom.AffineTransform
import java.awt.BasicStroke
import scala.collection.mutable.ArrayBuffer
import engine.Cst
import scala.swing.Reactions

/**
  * This class is the phase of the game where the player can buy
  * and add towers to defend the place.
  */
class MultiplayerDefensePhase extends DefensePhase { outer =>
	val mouseReleasedMultiplayerReaction : Reactions.Reaction = {
		case MouseReleased(_, point, _, _, _) =>
			val mousePos = mouseCursorPosition.toCellPoint
			if (mousePos.x <= (Cst.mapWidth / 2) - 1 && mousePos.y <= Cst.mapHeight - 1) {
				if (towerToAdd.isInstanceOf[TowerCompound]) {
					//Compound tower currently being placed
					if (Game.map.map(mousePos.x.toInt)(mousePos.y.toInt) == EmptyTowerCell && Game.gold >= towerToAdd.cost) {
						towerToAdd.pos = mousePos
						val comp = towerToAdd.asInstanceOf[TowerCompound]
						if (comp.isValidDistance(compoundsBuffer, comp.pos)) {
							compoundsBuffer += comp //add to buffer

							if (compoundsBuffer.length == comp.nb) { //All positions specified, we can add the tower itself
								Game.entities += comp.makeTower(compoundsBuffer)
								compoundsBuffer.clear
								Game.gold -= towerToAdd.cost
								towerToAdd = new ArmedTower //Back to default
							} else {
								val towerName = towerToAdd.getClass.getName
								towerToAdd = Class.forName(towerName).getConstructor().newInstance().asInstanceOf[Tower]
							}
						}
					}
				} else {
					if (((Game.map.map(mousePos.x.toInt)(mousePos.y.toInt) == EmptyTowerCell && !towerToAdd.isInstanceOf[WallTower])
					  || (Game.map.map(mousePos.x.toInt)(mousePos.y.toInt) == Path && towerToAdd.isInstanceOf[WallTower])
					  ) && Game.gold >= towerToAdd.cost) {
						towerToAdd.pos = mousePos
						Game.entities += towerToAdd
						if (towerToAdd.isInstanceOf[WallTower])
							Game.map.map(mousePos.x.toInt)(mousePos.y.toInt) = Wall
						else
							Game.map.map(mousePos.x.toInt)(mousePos.y.toInt) = OccupiedTowerCell
						Game.gold -= towerToAdd.cost
						val towerName = towerToAdd.getClass.getName
						towerToAdd = Class.forName(towerName).getConstructor().newInstance().asInstanceOf[Tower]
					}
				}
			}
	}

	reactions -= mouseReleasedReaction
	reactions += mouseReleasedMultiplayerReaction

	override def render(time: Double, delta: Double): Unit = {
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

		val mousePos = mouseCursorPosition.toCellPoint
		if (mousePos.x <= Cst.mapWidth - 1 && mousePos.y <= Cst.mapHeight - 1) {
			if ((Game.map.map(mousePos.x.toInt)(mousePos.y.toInt) == EmptyTowerCell && !towerToAdd.isInstanceOf[WallTower])
			  || (Game.map.map(mousePos.x.toInt)(mousePos.y.toInt) == Path && towerToAdd.isInstanceOf[WallTower])
			  && Game.gold >= towerToAdd.cost
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

		val gold = SpriteLoader.fromString(f"Gold : ${Game.gold}", 60, 15)
		Renderer.drawOnTextLayer(gold,  560, 45)
		// val exp = SpriteLoader.fromString(f"Exp : ${Game.experience}", 60, 15)
		// Renderer.drawOnTextLayer(exp, 560,  80)

		for (b <- buttons) {
			b.render(time, delta)
		}
	}

}