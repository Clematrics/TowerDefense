import scala.swing.event._
import java.awt.{Color, Dimension, Graphics2D, Point}
import java.awt.geom.AffineTransform
import java.awt.BasicStroke

class AttackPhase extends Level { outer =>
	reactions += {
		case MouseMoved(_, point, _) =>
			for(b <- buttons) b.onMoved(point)
		case MouseReleased(_, point, _, _, _) =>
			for(b <- buttons) b.onRelease(point)
	}

	val buttons : List[Button] = List(
		new Button(new Point(1200, 40), new Dimension(150, 60)) {
			listenTo(outer)
			sprite_back    = SpriteLoader.fromResource("menuButtonLarge.png")
			sprite_front   = SpriteLoader.fromString("nevermind", 150, 60, 30)
			action = () => {
				GamePanel.changeLevel("DefensePhase")
			}
		},
	)

	var time = -5.0

	var wave: Array[Tuple3[Double, Int, String]] = GameStatus.map.wave
	var entities: Array[Entity] = Array()

	override def tick(running_for: Double, delta: Double): Unit = {
		time += delta

		while (wave.length > 0 && wave.head._1 * 1000 <= time) {
			val (t, i, name) = wave.head
			val constr = Class.forName(name).getConstructor()
			entities +:= constr.newInstance().asInstanceOf[Entity]
			wave = wave.take(0)
		}

		for (e <- entities)
			e.tick(running_for, delta)
	}

	def render(g: Graphics2D, running_for: Double, delta: Double): Unit = {
		for(b <- buttons) {
			b.render(g, running_for, delta)
		}

		g.drawImage(GameStatus.map.mapImg, new AffineTransform(24, 0, 0, 24, 0, 0), null)
		for(cp <- GameStatus.map.checkpoints) {
			val stroke = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, Array(10, 5), 0)
			g.setStroke(stroke);
			g.setColor(new Color(255, 0, 255, 255))
			g.drawLine(cp.aX * 24, cp.aY * 24, cp.bX * 24, cp.bY * 24)
		}

		for(e <- entities) {
			e.render(g)
		}

		g.setColor(Color.PINK)
		g.drawString(f"$time%.1f ms", 0, 30)
	}
}