import engine.core.{GamePanel, Renderer, View}
import engine.helpers.{ScreenPoint}
import engine.loaders.SpriteLoader
import engine.interaction.{MouseHelper, Button}

import scala.swing.event._
import java.awt.{Color, Dimension, Graphics2D, Point}
import java.awt.geom.AffineTransform
import java.awt.BasicStroke
import scala.collection.mutable.ArrayBuffer
import engine.Cst

/**
  * This class is the phase of the game where the player can buy
  * and add towers to defend the place.
  */
class DefensePhase extends View { outer =>
	var mouseCursorPosition = new ScreenPoint(0, 0)

	reactions += {
		case MouseMoved(_, point, _) =>
			mouseCursorPosition = MouseHelper.fromMouse(point)
		case MouseReleased(_, point, _, _, _) =>
			val mousePos = mouseCursorPosition.toCellPoint
			if (mousePos.x <= Cst.mapWidth - 1 && mousePos.y <= Cst.mapHeight - 1) {
				if (isCompound) {
					//Compound tower currently being placed
					if (Game.map.map(mousePos.x.toInt)(mousePos.y.toInt) == EmptyTowerCell && Game.gold >= towerToAdd.cost) {
						towerToAdd.pos = mousePos
						val comp = towerToAdd.asInstanceOf[TowerCompound]
						compoundsBuffer += comp //add to buffer
		
						if (compoundsBuffer.length == comp.nb) {	//All positions specified, wa can add the tower itself
							println("arrivé")
							Game.entities += comp.makeTower(compoundsBuffer)
							compoundsBuffer.clear
							Game.gold -= towerToAdd.cost
							isCompound = false						//In order to avoid to reset the flag in each case
							towerToAdd = new ArmedTower				//Back to default
						} else {
							val towerName = towerToAdd.getClass.getName
							towerToAdd = Class.forName(towerName).getConstructor().newInstance().asInstanceOf[Tower]
						}
					}
				} else {
					if (Game.map.map(mousePos.x.toInt)(mousePos.y.toInt) == EmptyTowerCell && Game.gold >= towerToAdd.cost) {
						towerToAdd.pos = mousePos
						Game.entities += towerToAdd
						Game.map.map(mousePos.x.toInt)(mousePos.y.toInt) = OccupiedTowerCell
						Game.gold -= towerToAdd.cost
						val towerName = towerToAdd.getClass.getName
						towerToAdd = Class.forName(towerName).getConstructor().newInstance().asInstanceOf[Tower]
					}
				}
			}
	}

	var towerToAdd: Tower = new ArmedTower
	var compoundsBuffer: ArrayBuffer[TowerCompound] = ArrayBuffer()
	var isCompound: Boolean = false
	var dbg = false
	buttons ++= ArrayBuffer(
		new Button(new Point(605, 20), new Dimension(60, 30)) {
			spriteBack    = SpriteLoader.fromResource("menuButtonLarge.png")
			spriteFront   = SpriteLoader.fromString("Fight !", 60, 15)
			action = () => {
				GamePanel.changeView("AttackPhase")
			}
		},
		new Button(new Point(605, 140), new Dimension(60, 45)) {
			spriteBack    = SpriteLoader.fromResource("menuButtonLarge.png")
			spriteFront   = SpriteLoader.fromString("Blaster Tower", 60, 15)
			spriteTooltip = SpriteLoader.tooltip("Cost : 10 Gold\nRadius : 6\nReload time : 1s\nPower : 5\nA tower that shoots balls")
			action = () => {
				towerToAdd = new ArmedTower
			}
		},
		new Button(new Point(605, 190), new Dimension(60, 45)) {
			listenTo(outer)
			spriteBack    = SpriteLoader.fromResource("menuButtonLarge.png")
			spriteFront   = SpriteLoader.fromString("Laser Tower", 60, 15)
			spriteTooltip = SpriteLoader.tooltip("Cost : 20 Gold\nRadius : 6\nReload time : 2s\nPower : 20\nLAAASEERSS!")
			action = () => {
				towerToAdd = new LaserTower
			}
		},
		new Button(new Point(605, 240), new Dimension(60, 45)) {
			spriteBack    = SpriteLoader.fromResource("menuButtonLarge.png")
			spriteFront   = SpriteLoader.fromString("Chess Tower", 60, 15)
			spriteTooltip = SpriteLoader.tooltip("Cost : 15 Gold\nA noob tower that does nothing else than watching its enemies in the eyes")
			action = () => {
				towerToAdd = new ProtoTower
			}
		},
		new Button(new Point(605, 290), new Dimension(60, 45)) {
			spriteBack    = SpriteLoader.fromResource("menuButtonLarge.png")
			spriteFront   = SpriteLoader.fromString("Multi Tower", 60, 15)
			spriteTooltip = SpriteLoader.tooltip("Cost : 40 Gold\nRadius : 7\nReload time : 2s\nPower : 20\nCan shoot multiple enemies at the same time")
			action = () => {
				towerToAdd = new MultiTower
			}
		},
		new Button(new Point(605, 340), new Dimension(60, 45)) {
			spriteBack    = SpriteLoader.fromResource("menuButtonLarge.png")
			spriteFront   = SpriteLoader.fromString("Dual Tower", 60, 15)
			spriteTooltip = SpriteLoader.tooltip("Cost : 80 Gold\nPower : 20\nTwo towers making a laser barrier")
			action = () => {
				towerToAdd = new HalfDualTower
				isCompound = true		//For the moment, we use a flag telling us if it's several towers
			}
		},
		new Button(new Point(605, 380), new Dimension(60, 30)) {
			spriteBack    = SpriteLoader.fromResource("menuButtonLargeVar.png")
			spriteFront   = SpriteLoader.fromString("Go back", 60, 15)
			action = () => {
				GamePanel.changeView("CampaignMenu")
			}
		}
	)

	def render(time: Double, delta: Double): Unit = {
		Renderer.background.drawImage(Game.map.mapImg, new AffineTransform(3, 0, 0, 3, 0, 0), null)

		if (Renderer.debugMode) {
			Renderer.background.drawImage(Game.map.mapLayout, new AffineTransform(12, 0, 0, 12, 0, 0), null)
			for(cp <- Game.map.checkpoints) {
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
			
		/*Buffered towers*/
		for (c <- compoundsBuffer)
			c.render(time, delta)

		val mousePos = mouseCursorPosition.toCellPoint
		if (mousePos.x <= Cst.mapWidth - 1 && mousePos.y <= Cst.mapHeight - 1) {
			if (Game.map.map(mousePos.x.toInt)(mousePos.y.toInt) == EmptyTowerCell && Game.gold >= towerToAdd.cost) {
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
		Renderer.userInterface.drawImage(gold, new AffineTransform(1, 0, 0, 1, 580, 45), null)
		val exp = SpriteLoader.fromString(f"Exp : ${Game.experience}", 60, 15)
		Renderer.userInterface.drawImage(exp, new AffineTransform(1, 0, 0, 1, 580, 90), null)

		for(b <- buttons) {
			b.render(time, delta)
		}
	}
}