import scala.swing.event._
import java.awt.{Color, Dimension, Graphics2D, Point}
import java.awt.geom.AffineTransform
import java.awt.BasicStroke
import scala.collection.mutable.ArrayBuffer

class DefensePhase extends View { outer =>
	var mouseCursorPosition = new ScreenPoint(0, 0)

	reactions += {
		case MouseMoved(_, point, _) =>
			mouseCursorPosition = MouseHelper.fromMouse(point)
		case MouseReleased(_, point, _, _, _) =>
			val mousePos = mouseCursorPosition.toCellPoint
			if (mousePos.x <= 45 - 1 && mousePos.y <= 30 - 1) {
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

	var towerToAdd: Tower = new ArmedTower

	buttons ++= ArrayBuffer(
		new Button(new Point(1215, 40), new Dimension(120, 60)) {
			sprite_back    = SpriteLoader.fromResource("menuButtonLarge.png")
			sprite_front   = SpriteLoader.fromString("Fight !", 120, 30)
			action = () => {
				GamePanel.changeView("AttackPhase")
			}
		},
		new Button(new Point(1215, 280), new Dimension(120, 90)) {
			sprite_back    = SpriteLoader.fromResource("menuButtonLarge.png")
			sprite_front   = SpriteLoader.fromString("Blaster Tower", 120, 30)
			sprite_tooltip = SpriteLoader.tooltip("Cost : 10 Gold\nRadius : 6\nReload time : 1s\nPower : 5\nA tower that shoots balls")
			action = () => {
				towerToAdd = new ArmedTower
			}
		},
		new Button(new Point(1215, 380), new Dimension(120, 90)) {
			listenTo(outer)
			sprite_back    = SpriteLoader.fromResource("menuButtonLarge.png")
			sprite_front   = SpriteLoader.fromString("Laser Tower", 120, 30)
			sprite_tooltip = SpriteLoader.tooltip("Cost : 20 Gold\nRadius : 6\nReload time : 2s\nPower : 20\nLAAASEERSS!")
			action = () => {
				towerToAdd = new LaserTower
			}
		},
		new Button(new Point(1215, 480), new Dimension(120, 90)) {
			sprite_back    = SpriteLoader.fromResource("menuButtonLarge.png")
			sprite_front   = SpriteLoader.fromString("Chess Tower", 120, 30)
			sprite_tooltip = SpriteLoader.tooltip("Cost : 15 Gold\nA noob tower that does nothing else than watching its enemies in the eyes")
			action = () => {
				towerToAdd = new ProtoTower
			}
		},
		new Button(new Point(1215, 580), new Dimension(120, 90)) {
			sprite_back    = SpriteLoader.fromResource("menuButtonLarge.png")
			sprite_front   = SpriteLoader.fromString("Multi Tower", 120, 30)
			sprite_tooltip = SpriteLoader.tooltip("Cost : 40 Gold\nRadius : 7\nReload time : 2s\nPower : 20\nCan shoot multiple enemies at the same time")
			action = () => {
				towerToAdd = new MultiTower
			}
		},
		new Button(new Point(1215, 680), new Dimension(120, 90)) {
			sprite_back    = SpriteLoader.fromResource("menuButtonLarge.png")
			sprite_front   = SpriteLoader.fromString("Dual Tower", 120, 30)
			sprite_tooltip = SpriteLoader.tooltip("Cost : 80 Gold\nPower : 20\nTwo towers making a laser barrier")
			action = () => {
				towerToAdd = new DualTower
			}
		},
		new Button(new Point(1215, 760), new Dimension(120, 60)) {
			sprite_back    = SpriteLoader.fromResource("menuButtonLargeVar.png")
			sprite_front   = SpriteLoader.fromString("Go back", 120, 30)
			action = () => {
				GamePanel.changeView("CampaignMenu")
			}
		}
	)

	def render(running_for: Double, delta: Double): Unit = {
		Renderer.background.drawImage(Game.map.mapImg, new AffineTransform(6, 0, 0, 6, 0, 0), null)

		if (Renderer.debugMode) {
			Renderer.background.drawImage(Game.map.mapLayout, new AffineTransform(24, 0, 0, 24, 0, 0), null)
			for(cp <- Game.map.checkpoints) {
				val stroke = new BasicStroke(2)
				Renderer.debug.setStroke(stroke)
				Renderer.debug.setColor(new Color(255, 0, 255, 255))
				val spa = cp.a.toScreenPosition
				val spb = cp.b.toScreenPosition
				Renderer.debug.drawLine(spa.x, spa.y, spb.x, spb.y)
			}
		}

		for (t <- Game.entities)
			t.render(running_for, delta)

		val mousePos = mouseCursorPosition.toCellPoint
		if (mousePos.x <= 45 - 1 && mousePos.y <= 30 - 1) {
			if (Game.map.map(mousePos.x.toInt)(mousePos.y.toInt) == EmptyTowerCell && Game.gold >= towerToAdd.cost) {
				towerToAdd.pos = mousePos
				towerToAdd.render(running_for, delta)
			}
			else {
				val stroke = new BasicStroke(8)
				Renderer.userInterface.setStroke(stroke)
				Renderer.userInterface.setColor(Color.RED)
				val x = mousePos.x.toInt * 24
				val y = mousePos.y.toInt * 24
				Renderer.userInterface.drawLine(x, y, x + 24, y + 24)
				Renderer.userInterface.drawLine(x + 24, y, x, y + 24)
			}
		}

		val gold = SpriteLoader.fromString(f"Gold : ${Game.gold}", 120, 30)
		Renderer.userInterface.drawImage(gold, new AffineTransform(1, 0, 0, 1, 1160, 90), null)
		val exp = SpriteLoader.fromString(f"Exp : ${Game.experience}", 120, 30)
		Renderer.userInterface.drawImage(exp, new AffineTransform(1, 0, 0, 1, 1160, 180), null)

		for(b <- buttons) {
			b.render(running_for, delta)
		}
	}
}