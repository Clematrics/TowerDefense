import scala.swing.event._
import java.awt.{Color, Dimension, Graphics2D, Point}
import java.awt.geom.AffineTransform
import java.awt.BasicStroke

class DefensePhase extends Level { outer =>
	reactions += {
		case MouseMoved(_, point, _) =>
			for(b <- buttons) b.onMoved(point)
		case MouseReleased(_, point, _, _, _) =>
			for(b <- buttons) b.onRelease(point)
	}

	val buttons : List[Button] = List(
		new Button(new Point(1215, 40), new Dimension(120, 60)) {
			listenTo(outer)
			sprite_back    = SpriteLoader.fromResource("menuButtonLarge.png")
			sprite_front   = SpriteLoader.fromString("fight !", 120, 60, 30)
			action = () => {
				GamePanel.changeLevel("AttackPhase")
			}
		},
		new Button(new Point(1215, 290), new Dimension(120, 120)) {
			listenTo(outer)
			sprite_back    = SpriteLoader.fromResource("menuButtonLarge.png")
			sprite_front   = SpriteLoader.fromString("tower 1", 120, 120, 30)
			action = () => {
				
			}
		},
		new Button(new Point(1215, 430), new Dimension(120, 120)) {
			listenTo(outer)
			sprite_back    = SpriteLoader.fromResource("menuButtonLarge.png")
			sprite_front   = SpriteLoader.fromString("tower 2", 120, 120, 30)
			action = () => {
				
			}
		},
		new Button(new Point(1215, 570), new Dimension(120, 120)) {
			listenTo(outer)
			sprite_back    = SpriteLoader.fromResource("menuButtonLarge.png")
			sprite_front   = SpriteLoader.fromString("tower 3", 120, 120, 30)
			action = () => {
				
			}
		},
		new Button(new Point(1215, 680), new Dimension(120, 60)) {
			listenTo(outer)
			sprite_back    = SpriteLoader.fromResource("menuButtonLarge.png")
			sprite_front   = SpriteLoader.fromString("go back", 120, 60, 30)
			action = () => {
				GamePanel.changeLevel("CampaignMenu")
			}
		}
	)


	def render(g: Graphics2D, running_for: Double, delta: Double): Unit = {

		g.drawImage(GameStatus.map.mapImg, new AffineTransform(24, 0, 0, 24, 0, 0), null)
		for(cp <- GameStatus.map.checkpoints) {
			val stroke = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, Array(3, 1), 0)
			g.setStroke(stroke);
			g.setColor(new Color(255, 0, 255, 255))
			g.drawLine(cp.aX * 24, cp.aY * 24, cp.bX * 24, cp.bY * 24)
		}

		val gold = SpriteLoader.fromString(f"Gold : ${GameStatus.gold}", 120, 30, 30)
		g.drawImage(gold, new AffineTransform(1, 0, 0, 1, 1155, 90), null)
		val exp = SpriteLoader.fromString(f"Exp : ${GameStatus.experience}", 120, 30, 30)
		g.drawImage(exp, new AffineTransform(1, 0, 0, 1, 1155, 140), null)

		for(b <- buttons) {
			b.render(g, running_for, delta)
		}
	}
}