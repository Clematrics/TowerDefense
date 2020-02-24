import scala.swing.event._
import java.awt.{Color, Dimension, Graphics2D, Point}
import java.awt.geom.AffineTransform
import java.awt.BasicStroke

/**
  * This class is the phase of the game (seen as a Level class) where
  * enemies attack and towers defend the player.
  */
class AttackPhase extends Level { outer =>
	val r = scala.util.Random
	var time = -5.0
	var wave: Array[Tuple3[Double, Int, String]] = GameStatus.map.wave
	var entities: Array[Entity] = Array()
	
	reactions += {
		case MouseMoved(_, point, _) =>
			for(b <- buttons) b.onMoved(point)
		case MouseReleased(_, point, _, _, _) =>
			for(b <- buttons) b.onRelease(point)
	}

	val buttons : List[Button] = List(
		new Button(new Point(1200, 40), new Dimension(150, 60)) {
			sprite_back    = SpriteLoader.fromResource("menuButtonLarge.png")
			sprite_front   = SpriteLoader.fromString("nevermind", 150, 60, 30)
			action = () => {
				GamePanel.changeLevel("DefensePhase")
			}
		},
	)

	override def tick(running_for: Double, delta: Double): Unit = {
		if (GameStatus.health <= 0) {
			GamePanel.changeLevel("LoseMenu")
		}

		time += delta

		while (wave.length > 0 && wave.head._1 * 1000 <= time) {
			val (t, i, name) = wave.head
			val constr = Class.forName(name).getConstructor()
			val enemy: Enemy = constr.newInstance().asInstanceOf[Enemy]
			
			println(name)//DEBUG
			
			//Random choice of the target on the portal segment for spawn location
			val cp = GameStatus.map.checkpoints(i)
			enemy.pos = new CellPosition(cp.aX + r.nextFloat * (cp.bX - cp.aX), cp.aY + r.nextFloat * (cp.bY - cp.aY))
			enemy.targetedCheckpoint = cp.next
			
			//Random choice of the target on the portal segment for destination
			val cpp = GameStatus.map.checkpoints(cp.next)
			enemy.targetedCellPoint = new CellPosition(cpp.aX + r.nextFloat * (cpp.bX - cpp.aX), cpp.aY + r.nextFloat * (cpp.bY - cpp.aY))
			entities +:= enemy.asInstanceOf[Entity]

			wave = wave.tail
		}

		for (e <- entities)
			e.tick(running_for, delta)

		entities = entities.filter((p: Entity) => p.valid)//QUESTION : et les monstres morts?

		if (wave.length == 0 && entities.length == 0) {
			GamePanel.changeLevel("WinMenu")
		}
	}

	/**
	  * Rendering of the game, with the map and all entities
	  *
	  * @param g A Graphics2D object representing the drawing surface
	  * @param running_for Total time the game has been running
	  * @param delta 
	  */
	def render(g: Graphics2D, running_for: Double, delta: Double): Unit = {
		for(b <- buttons) {
			b.render(g, running_for, delta)
		}

		g.drawImage(GameStatus.map.mapImg, new AffineTransform(24, 0, 0, 24, 0, 0), null)
		for(cp <- GameStatus.map.checkpoints) {
			val stroke = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, Array(10, 5), 0)
			g.setStroke(stroke)
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