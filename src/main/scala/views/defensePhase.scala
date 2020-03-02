import scala.swing.event._
import java.awt.{Color, Dimension, Graphics2D, Point}
import java.awt.geom.AffineTransform
import java.awt.BasicStroke

class DefensePhase extends View { outer =>
	var mouseCursorPosition = new ScreenPoint(0, 0)

	reactions += {
		case MouseMoved(_, point, _) =>
			mouseCursorPosition = new ScreenPoint(point)
			for(b <- buttons) b.onMoved(point)
		case MouseReleased(_, point, _, _, _) =>
			for(b <- buttons) b.onRelease(point)

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
	var selectedTower = new Color(255, 255, 0, 255)

	val buttons : List[Button] = List(
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
				selectedTower = new Color(255, 255, 0, 255)
				towerToAdd = new ArmedTower
			}
		},
		new Button(new Point(1215, 380), new Dimension(120, 90)) {
			listenTo(outer)
			sprite_back    = SpriteLoader.fromResource("menuButtonLarge.png")
			sprite_front   = SpriteLoader.fromString("Laser Tower", 120, 30)
			sprite_tooltip = SpriteLoader.tooltip("Cost : 20 Gold\nRadius : 6\nReload time : 2s\nPower : 20\nLAAASEERSS!")
			action = () => {
				selectedTower = new Color(0, 255, 255, 255)
				towerToAdd = new LaserTower
			}
		},
		new Button(new Point(1215, 480), new Dimension(120, 90)) {
			sprite_back    = SpriteLoader.fromResource("menuButtonLarge.png")
			sprite_front   = SpriteLoader.fromString("Chess Tower", 120, 30)
			sprite_tooltip = SpriteLoader.tooltip("Cost : 15 Gold\nA noob tower that does nothing else than watching its enemies in the eyes")
			action = () => {
				selectedTower = new Color(255, 0, 255, 255)
				towerToAdd = new ProtoTower
			}
		},
		new Button(new Point(1215, 580), new Dimension(120, 90)) {
			sprite_back    = SpriteLoader.fromResource("menuButtonLarge.png")
			sprite_front   = SpriteLoader.fromString("Multi Tower", 120, 30)
			sprite_tooltip = SpriteLoader.tooltip("Cost : 40 Gold\nRadius : 7\nReload time : 2s\nPower : 20\nCan shoot multiple enemies at the same time")
			action = () => {
				selectedTower = new Color(255, 0, 255, 255)
				towerToAdd = new MultiTower
			}
		},
		new Button(new Point(1215, 680), new Dimension(120, 90)) {
			sprite_back    = SpriteLoader.fromResource("menuButtonLarge.png")
			sprite_front   = SpriteLoader.fromString("Dual Tower", 120, 30)
			sprite_tooltip = SpriteLoader.tooltip("Cost : 80 Gold\nPower : 20\nTwo towers making a laser barrier")
			action = () => {
				selectedTower = new Color(255, 0, 255, 255)
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

	def render(g: Graphics2D, running_for: Double, delta: Double): Unit = {
		g.drawImage(Game.map.mapImg, new AffineTransform(6, 0, 0, 6, 0, 0), null)

		if (debugMode) {
			g.drawImage(Game.map.mapLayout, new AffineTransform(24, 0, 0, 24, 0, 0), null)
			for(cp <- Game.map.checkpoints) {
				val stroke = new BasicStroke(2)
				g.setStroke(stroke)
				g.setColor(new Color(255, 0, 255, 255))
				val spa = cp.a.toScreenPosition
				val spb = cp.b.toScreenPosition
				g.drawLine(spa.x, spa.y, spb.x, spb.y)
			}
		}

		for (t <- Game.entities)
			t.render(g)

		val mousePos = mouseCursorPosition.toCellPoint
		if (mousePos.x <= 45 - 1 && mousePos.y <= 30 - 1) {
			if (Game.map.map(mousePos.x.toInt)(mousePos.y.toInt) == EmptyTowerCell && Game.gold >= towerToAdd.cost) {
				g.setColor(selectedTower)
				towerToAdd.pos = mousePos
				towerToAdd.render(g)
			}
			else {
				val stroke = new BasicStroke(8)
				g.setStroke(stroke)
				g.setColor(Color.RED)
				val x = mousePos.x.toInt * 24
				val y = mousePos.y.toInt * 24
				g.drawLine(x, y, x + 24, y + 24)
				g.drawLine(x + 24, y, x, y + 24)
			}
		}

		val gold = SpriteLoader.fromString(f"Gold : ${Game.gold}", 120, 30)
		g.drawImage(gold, new AffineTransform(1, 0, 0, 1, 1160, 90), null)
		val exp = SpriteLoader.fromString(f"Exp : ${Game.experience}", 120, 30)
		g.drawImage(exp, new AffineTransform(1, 0, 0, 1, 1160, 180), null)

		for(b <- buttons) {
			b.render(g, running_for, delta)
		}
	}
}