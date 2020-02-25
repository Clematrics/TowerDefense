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
				towerToAdd.pos = mousePos
				Game.entities.+=(towerToAdd)
				selectedTower = new Color(255, 255, 0, 255)
				towerToAdd = new ArmedTower
			}
	}

	var towerToAdd: Tower = new ArmedTower
	var selectedTower = new Color(255, 255, 0, 255)

	val buttons : List[Button] = List(
		new Button(new Point(1215, 40), new Dimension(120, 60)) {
			sprite_back    = SpriteLoader.fromResource("menuButtonLarge.png")
			sprite_front   = SpriteLoader.fromString("fight !", 120, 60, 30)
			action = () => {
				GamePanel.changeView("AttackPhase")
			}
		},
		new Button(new Point(1215, 290), new Dimension(120, 120)) {
			sprite_back    = SpriteLoader.fromResource("menuButtonLarge.png")
			sprite_front   = SpriteLoader.fromString("Blaster Tower", 120, 120, 30)
			action = () => {
				selectedTower = new Color(255, 255, 0, 255)
				towerToAdd = new ArmedTower
			}
		},
		new Button(new Point(1215, 430), new Dimension(120, 120)) {
			listenTo(outer)
			sprite_back    = SpriteLoader.fromResource("menuButtonLarge.png")
			sprite_front   = SpriteLoader.fromString("Laser Tower", 120, 120, 30)
			action = () => {
				selectedTower = new Color(0, 255, 255, 255)
				towerToAdd = new LaserTower
			}
		},
		new Button(new Point(1215, 570), new Dimension(120, 120)) {
			sprite_back    = SpriteLoader.fromResource("menuButtonLarge.png")
			sprite_front   = SpriteLoader.fromString("Chess Tower", 120, 120, 30)
			action = () => {
				selectedTower = new Color(255, 0, 255, 255)
				towerToAdd = new ProtoTower
			}
		},
		new Button(new Point(1215, 680), new Dimension(120, 60)) {
			sprite_back    = SpriteLoader.fromResource("menuButtonLarge.png")
			sprite_front   = SpriteLoader.fromString("go back", 120, 60, 30)
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

		val mousePos = mouseCursorPosition.toCellPoint
		if (mousePos.x <= 45 - 1 && mousePos.y <= 30 - 1) {
			g.setColor(selectedTower)
			g.fillRect(mousePos.x.toInt * 24, mousePos.y.toInt * 24, 24, 24)
		}

		val gold = SpriteLoader.fromString(f"Gold : ${Game.gold}", 120, 30, 30)
		g.drawImage(gold, new AffineTransform(1, 0, 0, 1, 1155, 90), null)
		val exp = SpriteLoader.fromString(f"Exp : ${Game.experience}", 120, 30, 30)
		g.drawImage(exp, new AffineTransform(1, 0, 0, 1, 1155, 140), null)

		for(b <- buttons) {
			b.render(g, running_for, delta)
		}
	}
}